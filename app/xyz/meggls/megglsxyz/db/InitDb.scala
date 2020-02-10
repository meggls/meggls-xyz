package xyz.meggls.megglsxyz.db

import java.sql.Connection

import akka.Done
import anorm.SQL
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import xyz.meggls.megglsxyz.employment.EmploymentManager
import xyz.meggls.megglsxyz.util.LogUtil

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class InitDb @Inject()(db: Database) {

    import InitDb._

    def resetDb: Future[Done] = {
        val dbResult = Future{db.withConnection{ implicit c: Connection =>

            // setup schemas
            //DbCalls.createSchemas

            // setup tables
            DbCalls.createEmployment
            DbCalls.createEmploymentPosition
            DbCalls.createEmploymentDuty

            // insert data
            EmploymentManager.InitData.data.map { empExp =>
                val experienceInsert = EmploymentManager.DbCalls.insertEmployment(empExp)
                experienceInsert.map { experienceId =>
                    empExp.positions.map(_.map { empPos =>
                        val positionInsert = EmploymentManager.DbCalls.insertEmploymentPosition(experienceId, empPos)
                        positionInsert.map { positionId =>
                            empPos.duties.map(_.map { empDuty =>
                                EmploymentManager.DbCalls.insertEmploymentDuty(positionId, empDuty)
                            })
                        }
                    })
                }
            }

        }}(DbExecutionContext.ctx)
        dbResult.map{_ => Done}
    }

    resetDb

}

object InitDb extends LogUtil {

    object DbCalls {

        import SchemaDefinition._

        /*
        * WARNING:
        * These CREATE and DROP TABLE scripts are currently specific to MySql
        * Specifically:
        *  - DROP TABLE IF EXISTS
        *  - AUTO_INCREMENT
        * Leaving off FK constraints for now as it over-complicates the drops
        */

        private def dropIfExists(tableName: String)(implicit c: Connection): Boolean = {
            //ONLY USE INTERNALLY!! OPEN TO SQL INJECTION WITH STRING INTERPOLATION
            val sql =
                s"""
                  |DROP TABLE IF EXISTS $tableName;
                """.stripMargin
            SQL(sql).execute()
        }

        def createSchemas(implicit c: Connection): Boolean = {
            log.trace("Starting createSchemas")
            val result = SCHEMA_LIST.map { schema =>
                val sql = s"CREATE SCHEMA $schema"
                SQL(sql).execute()
            }.reduce(_&&_)
            log.trace("Finished createSchemas")
            result
        }

        def createEmployment(implicit c: Connection): Boolean = {
            log.trace("Starting createEmployment")
            val sql =
                s"""
                  |CREATE TABLE $TABLE_EMPLOYMENT_EXPERIENCE (
                  |  id int NOT NULL AUTO_INCREMENT,
                  |  company varchar(255) NOT NULL,
                  |  startDate int NOT NULL,
                  |  endDate int,
                  |  CONSTRAINT PK_Employment PRIMARY KEY (id)
                  |);
                """.stripMargin
            dropIfExists(TABLE_EMPLOYMENT_EXPERIENCE)
            val result = SQL(sql).execute()
            log.trace("Finished createEmployment")
            result
        }

        def createEmploymentPosition(implicit c: Connection): Boolean = {
            val sql =
                s"""
                   |CREATE TABLE $TABLE_EMPLOYMENT_POSITION (
                   |  id int NOT NULL AUTO_INCREMENT,
                   |  experienceId int NOT NULL,
                   |  title varchar(255) NOT NULL,
                   |  startDate int NOT NULL,
                   |  endDate int,
                   |  CONSTRAINT PK_EmploymentPosition PRIMARY KEY (id)
                   |);
                 """.stripMargin
            dropIfExists(TABLE_EMPLOYMENT_POSITION)
            SQL(sql).execute()
        }

        def createEmploymentDuty(implicit c: Connection): Boolean = {
            val sql =
                s"""
                   |CREATE TABLE $TABLE_EMPLOYMENT_DUTY (
                   |  id int NOT NULL AUTO_INCREMENT,
                   |  positionId int NOT NULL,
                   |  description varchar(255) NOT NULL,
                   |  priority int NOT NULL,
                   |  CONSTRAINT PK_EmploymentDuty PRIMARY KEY (id)
                   |);
                 """.stripMargin
            dropIfExists(TABLE_EMPLOYMENT_DUTY)
            SQL(sql).execute()
        }

    }
}
