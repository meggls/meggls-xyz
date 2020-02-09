package xyz.meggls.megglsxyz.employment

import java.sql.Connection

import akka.Done
import anorm.{NamedParameter, SQL}
import javax.inject.Inject
import play.api.db.Database

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import xyz.meggls.megglsxyz.db.{DbExecutionContext, SchemaDefinition}

class EmploymentManager @Inject()(db: Database) {

    import EmploymentManager._

    def getAllEmployment: Future[List[EmploymentExperience]] = {
        val dbResults = Future { db.withConnection { implicit c =>
            val experiences = DbCalls.selectAllEmploymentExperience
            val positions = DbCalls.selectAllEmploymentPosition
            val duties = DbCalls.selectAllEmploymentDuty
            (experiences, positions, duties)
        }}(DbExecutionContext.ctx)
        for {
            (experiences, positions, duties) <- dbResults
            expPositions = positions.groupBy(_.experienceId)
            posDuties = duties.groupBy(_.positionId).mapValues(_.map(EmploymentDuty.fromDb))
            positionsResult = expPositions.mapValues(_.map { position =>
                val duties = posDuties.getOrElse(position.id, List.empty)
                EmploymentPosition.fromDb(duties)(position)
            })
            result = experiences.map { experience =>
                val positions = positionsResult.getOrElse(experience.id, List.empty)
                EmploymentExperience.fromDb(positions)(experience)
            }
        } yield  result
    }

    def getEmploymentExperience(experienceId: Long): Future[Option[EmploymentExperience]] = {
        val dbResults = Future { db.withConnection { implicit c =>
            val experience = DbCalls.selectEmploymentExperience(experienceId)
            val positionDuties = if (experience.isDefined)
                DbCalls.selectEmploymentPositionByExperience(experienceId).map { posDb =>
                    val dutiesDb = DbCalls.selectEmploymentDutyByPosition(posDb.id)
                    (posDb, dutiesDb)
                }
            else
                List.empty
            (experience, positionDuties)
        }}(DbExecutionContext.ctx)
        for {
            (experience, positionDuties) <- dbResults
            positions = positionDuties.map { case (posDb, dutiesDb) =>
                val duties = dutiesDb.map(EmploymentDuty.fromDb)
                EmploymentPosition.fromDb(duties)(posDb)
            }
        } yield experience.map(EmploymentExperience.fromDb(positions))
    }

    def getEmploymentPositions(experienceId: Long): Future[List[EmploymentPosition]] = {
        ???
    }

    def getEmploymentPosition(experienceId: Long, positionId: Long): Future[Option[EmploymentPosition]] = {
        //TODO: validate that position is within experience
        val dbResult = Future { db.withConnection { implicit c =>
            val position = DbCalls.selectEmploymentPosition(positionId)
            val duties = if (position.isDefined) DbCalls.selectEmploymentDutyByPosition(positionId) else List.empty
            (position, duties)
        }}(DbExecutionContext.ctx)
        for {
            (positionResult, dutiesResult) <- dbResult
            duties = dutiesResult.map(EmploymentDuty.fromDb)
        } yield positionResult.map(EmploymentPosition.fromDb(duties))
    }

    def getEmploymentPositionDuties(experienceId: Long, positionId: Long): Future[List[EmploymentDuty]] = {
        ???
    }

    def getEmploymentPositionDuty(experienceId: Long, positionId: Long, dutyId: Long): Future[Option[EmploymentDuty]] = {
        //TODO: validate that position is within experience
        val dbResult = Future { db.withConnection { implicit c =>
            DbCalls.selectEmploymentDuty(dutyId)
        }}(DbExecutionContext.ctx)
        dbResult.map(_.map(EmploymentDuty.fromDb))
    }

    def addEmploymentExperience(empExp: EmploymentExperience): Future[Option[Long]] = {
        Future { db.withConnection { implicit c =>
            val experienceInsert = DbCalls.insertEmploymentExperience(empExp)
            experienceInsert.map { experienceId =>
                empExp.positions.map { empPos =>
                    val positionInsert = DbCalls.insertEmploymentPosition(experienceId, empPos)
                    positionInsert.map { positionId =>
                        empPos.duties.map { empDuty =>
                            DbCalls.insertEmploymentDuty(positionId, empDuty)
                        }
                    }
                }
            }
            experienceInsert
        }}(DbExecutionContext.ctx)
    }

    def addEmploymentPosition(experienceId: Long, empPos: EmploymentPosition): Future[Option[Long]] = {
        Future { db.withConnection { implicit c =>
            val positionInsert = DbCalls.insertEmploymentPosition(experienceId, empPos)
            positionInsert.map { positionId =>
                empPos.duties.map { empDuty =>
                    DbCalls.insertEmploymentDuty(positionId, empDuty)
                }
            }
            positionInsert
        }}(DbExecutionContext.ctx)
    }

    def addEmploymentDuty(positionId: Long, empDuty: EmploymentDuty): Future[Option[Long]] = {
        Future { db.withConnection { implicit c =>
            DbCalls.insertEmploymentDuty(positionId, empDuty)
        }}(DbExecutionContext.ctx)
    }

    def updateEmploymentExperience(experienceId: Long, newExp: EmploymentExperience): Future[Int] = {
        Future { db.withConnection { implicit c =>
            ???
        }}(DbExecutionContext.ctx)
    }

    def updateEmploymentPosition(positionId: Long, newPos: EmploymentPosition): Future[Int] = {
        Future { db.withConnection { implicit c =>
            ???
        }}(DbExecutionContext.ctx)
    }

    def updateEmploymentDuty(dutyId: Long, newDuty: EmploymentDuty): Future[Int] = {
        Future { db.withConnection { implicit c =>
            ???
        }}(DbExecutionContext.ctx)
    }

    def deleteEmploymentExperience(experienceId: Long): Future[Done] = {
        Future { db.withConnection { implicit c =>
            ???
        }}(DbExecutionContext.ctx)
    }

    def deleteEmploymentPosition(positionId: Long): Future[Int] = {
        Future { db.withConnection { implicit c =>
            ???
        }}(DbExecutionContext.ctx)
    }

    def deleteEmploymentDuty(dutyId: Long): Future[Int] = {
        Future { db.withConnection { implicit c =>
            ???
        }}(DbExecutionContext.ctx)
    }

}

object EmploymentManager {

    object DbCalls {

        import SchemaDefinition._

        def selectAllEmploymentExperience(implicit c: Connection): List[ExperienceDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_EXPERIENCE
                 """.stripMargin
            SQL(sql).as(ExperienceDb.sqlFormat.*)
        }

        def selectEmploymentExperience(experienceId: Long)(implicit c: Connection): Option[ExperienceDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_EXPERIENCE
                   |WHERE id = {experienceId}
                 """.stripMargin
            val params: List[NamedParameter] = List("experienceId" -> experienceId)
            SQL(sql).on(params:_*).as(ExperienceDb.sqlFormat.singleOpt)
        }

        def selectAllEmploymentPosition(implicit c: Connection): List[PositionDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_POSITION
                 """.stripMargin
            SQL(sql).as(PositionDb.sqlFormat.*)
        }

        def selectEmploymentPositionByExperience(experienceId: Long)(implicit c: Connection): List[PositionDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_POSITION
                   |WHERE experienceId = {experienceId}
                 """.stripMargin
            val params: List[NamedParameter] = List("experienceId" -> experienceId)
            SQL(sql).on(params:_*).as(PositionDb.sqlFormat.*)
        }

        def selectEmploymentPosition(positionId: Long)(implicit c: Connection): Option[PositionDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_POSITION
                   |WHERE id = {positionId}
                 """.stripMargin
            val params: List[NamedParameter] = List("positionId" -> positionId)
            SQL(sql).on(params:_*).as(PositionDb.sqlFormat.singleOpt)
        }

        def selectAllEmploymentDuty(implicit c: Connection): List[DutyDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_DUTY
                 """.stripMargin
            SQL(sql).as(DutyDb.sqlFormat.*)
        }

        def selectEmploymentDutyByPosition(positionId: Long)(implicit c: Connection): List[DutyDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_DUTY
                   |WHERE positionId = {positionId}
                 """.stripMargin
            val params: List[NamedParameter] = List("positionId" -> positionId)
            SQL(sql).on(params:_*).as(DutyDb.sqlFormat.*)
        }

        def selectEmploymentDuty(dutyId: Long)(implicit c: Connection): Option[DutyDb] = {
            val sql =
                s"""
                   |SELECT *
                   |FROM $TABLE_EMPLOYMENT_DUTY
                   |WHERE id = {dutyId}
                 """.stripMargin
            val params: List[NamedParameter] = List("dutyId" -> dutyId)
            SQL(sql).on(params:_*).as(DutyDb.sqlFormat.singleOpt)
        }

        def insertEmploymentExperience(exp: EmploymentExperience)(implicit c: Connection): Option[Long] = {
            val sql =
                s"""
                  |INSERT INTO $TABLE_EMPLOYMENT_EXPERIENCE (company, startDate, endDate)
                  |  VALUES ({company}, {startDate}, {endDate})
                """.stripMargin
            val params: List[NamedParameter] = List(
                "company" -> exp.company,
                "startDate" -> exp.startDate,
                "endDate" -> exp.endDate
            )
            SQL(sql).on(params:_*).executeInsert[Option[Long]]()
        }

        def insertEmploymentPosition(experienceId: Long, pos: EmploymentPosition)(implicit c: Connection): Option[Long] = {
            val sql =
                s"""
                  |INSERT INTO $TABLE_EMPLOYMENT_POSITION (experienceId, title, startDate, endDate)
                  |  VALUES ({experienceId}, {title}, {startDate}, {endDate})
                """.stripMargin
            val params: List[NamedParameter] = List(
                "experienceId" -> experienceId,
                "title" -> pos.title,
                "startDate" -> pos.startDate,
                "endDate" -> pos.endDate
            )
            SQL(sql).on(params:_*).executeInsert[Option[Long]]()
        }

        def insertEmploymentDuty(positionId: Long, duty: EmploymentDuty)(implicit c: Connection): Option[Long] = {
            val sql =
                s"""
                   |INSERT INTO $TABLE_EMPLOYMENT_DUTY (positionId, description, priority)
                   |  VALUES ({positionId}, {description}, {priority})
                 """.stripMargin
            val params: List[NamedParameter] = List(
                "positionId" -> positionId,
                "description" -> duty.description,
                "priority" -> duty.priority
            )
            SQL(sql).on(params:_*).executeInsert[Option[Long]]()
        }

    }

    object InitData {
        val data: List[EmploymentExperience] = List(
            EmploymentExperience(
                company = "Decentrix, Inc.",
                startDate = 20150501,
                endDate = None,
                positions = List(
                    EmploymentPosition(
                        title = "Senior Analytics Engineer",
                        startDate = 20170701,
                        endDate = None,
                        duties = List(
                            EmploymentDuty(description = "Design, implement and test efficient microservices in Scala using Play Framework", priority = 1),
                            EmploymentDuty(description = "Improve API performance by researching, testing and implementing alternative tools and architectures including caches, queues, and various data stores such as RabbitMQ, Memcached, AWS S3, Microsoft SQL Server, Reactive MongoDB", priority = 2),
                            EmploymentDuty(description = "Improve observability into each service to reduce uncertainty and debugging time with tools and standards such as versioning, structured logging, Grafana dashboards, Prometheus metrics and alerting, AWS Log Insights, Honey-comb tracing", priority = 3),
                            EmploymentDuty(description = "Configure and support deployments for staging, UAT, and production across multiple clients using Jenkins and AWS", priority = 4),
                            EmploymentDuty(description = "Develop in a distributed and scalable microservice environment using Docker, Consul, Registrator", priority = 5),
                            EmploymentDuty(description = "Collaborate cross-team to help improve tooling and standards throughout the organization", priority = 6)
                        )
                    ),
                    EmploymentPosition(
                        title = "BI Consultant, Senior BI Consultant",
                        startDate = 20150501,
                        endDate = Some(20170701),
                        duties = List(
                            EmploymentDuty(description = "Developed ETL processes in SSIS to automate data flows and incorporate business logic", priority = 1),
                            EmploymentDuty(description = "Developed OLAP multi-dimensional cubes and Tabular data model structures with custom calculated measures in SSAS to enhance analytic capabilities", priority = 2),
                            EmploymentDuty(description = "Managed client relations through prompt and respectful communications regarding current and upcoming projects", priority = 3),
                            EmploymentDuty(description = "Contributed to drafting sales documents such as SOWs and RFPs for potential upcoming projects", priority = 4),
                            EmploymentDuty(description = "Addressed potential shortcomings in current logic or in the data warehouse structure where the current solution does not sufficiently address business needs", priority = 5),
                            EmploymentDuty(description = "Created custom reports and tools using MDX and DAX via SSRS and Excel with data connections for client use", priority = 6)
                        )
                    )
                )
            ),
            EmploymentExperience(
                company = "Rally Software Development",
                startDate = 20141101,
                endDate = Some(20150501),
                positions = List(
                    EmploymentPosition(
                        title = "Marketing Performance and Operations Intern",
                        startDate = 20141101,
                        endDate = Some(20150501),
                        duties = List(
                            EmploymentDuty(description = "Monitored incoming data from Salesforce.com and Eloqua to find and resolve automation errors and data entry errors", priority = 1),
                            EmploymentDuty(description = "Generated reports through the Salesforce.com GUI and MySQL databases", priority = 2),
                            EmploymentDuty(description = "Configured discount codes for the eCommerce system through Commerce Kickstart", priority = 3),
                            EmploymentDuty(description = "Scheduled presentations and managed recordings for weekly meetings through WebEx and Google Hangouts", priority = 4),
                            EmploymentDuty(description = "Processed contracts and purchase orders for the Marketing department through NetSuite, Docusign, and email", priority = 5)
                        )
                    )
                )
            )
        )
    }

}
