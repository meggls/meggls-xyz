package xyz.meggls.megglsxyz.employment

import java.sql.Connection

import akka.Done
import anorm.{NamedParameter, SQL}

import scala.concurrent.Future
import xyz.meggls.megglsxyz.db.SchemaDefinition

class EmploymentManager {

    import EmploymentManager._

    def getAllEmployment: Future[List[EmploymentExperience]] = ???

    def getEmploymentExperience(id: Int): Future[EmploymentExperience] = ???

    def getEmploymentPosition(positionId: Int): Future[EmploymentPosition] = ???

    def getEmploymentDuty(dutyId: Int): Future[EmploymentDuty] = ???

    def addEmploymentExperience(empExp: EmploymentExperience): Future[Int] = ???

    def addEmploymentPosition(experienceId: Int, empPos: EmploymentPosition): Future[Int] = ???

    def addEmploymentDuty(experienceId: Int, positionId: Int, duty: EmploymentDuty): Future[Int] = ???

    def updateEmploymentExperience(experienceId: Int, newExp: EmploymentExperience): Future[Done] = ???

    def updateEmploymentPosition(positionId: Int, newPos: EmploymentPosition): Future[Int] = ???

    def updateEmploymentDuty(dutyId: Int, newDuty: EmploymentDuty): Future[Int] = ???

    def deleteEmploymentExperience(experienceId: Int): Future[Done] = ???

    def deleteEmploymentPosition(positionId: Int): Future[Int] = ???

    def deleteEmploymentDuty(dutyId: Int): Future[Int] = ???

}

object EmploymentManager {

    object DbCalls {

        import SchemaDefinition._

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
