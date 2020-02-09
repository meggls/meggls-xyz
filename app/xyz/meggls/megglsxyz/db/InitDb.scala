package xyz.meggls.megglsxyz.db

import java.sql.Connection

import anorm.SQL
import javax.inject.{Inject, Singleton}
import play.api.db.Database

@Singleton
class InitDb @Inject()(db: Database) {

    import InitDb._

    db.withConnection{ implicit c: Connection =>

        // setup schemas
        //DbCalls.createSchemas

        // setup tables
        DbCalls.createEmploymentExperience
        DbCalls.createEmploymentPosition
        DbCalls.createEmploymentDuty
    }

}

object InitDb {

    object DbCalls {

        import SchemaDefinition._

        private def dropIfExists(tableName: String)(implicit c: Connection): Boolean = {
            //ONLY USE INTERNALLY!! OPEN TO SQL INJECTION WITH STRING INTERPOLATION
            val sql = //WARNING: vendor-specific to MySql as free JawsDB doesn't grant me access to info schema
                s"""
                  |DROP TABLE IF EXISTS $tableName;
                """.stripMargin
            SQL(sql).execute()
        }

        def createSchemas(implicit c: Connection): Boolean = {
            SCHEMA_LIST.map { schema =>
                val sql = s"CREATE SCHEMA $schema"
                SQL(sql).execute()
            }.reduce(_&&_)
        }

        def createEmploymentExperience(implicit c: Connection): Boolean = {
            println("In createEmploymentExperience")
            val sql =
                s"""
                  |CREATE TABLE $TABLE_EMPLOYMENT_EXPERIENCE (
                  |  id int NOT NULL,
                  |  company varchar(255) NOT NULL,
                  |  startDate int NOT NULL,
                  |  endDate int,
                  |  CONSTRAINT PK_EmploymentExperience PRIMARY KEY (id)
                  |);
                """.stripMargin
            dropIfExists(TABLE_EMPLOYMENT_EXPERIENCE)
            val result = SQL(sql).execute()
            println("Finished createEmploymentExperience")
            result
        }

        def createEmploymentPosition(implicit c: Connection): Boolean = {
            val fk = "" //,CONSTRAINT FK_ExperiencePosition FOREIGN KEY (experienceId) REFERENCES $TABLE_EMPLOYMENT_EXPERIENCE(id)
            val sql =
                s"""
                   |CREATE TABLE $TABLE_EMPLOYMENT_POSITION (
                   |  id int NOT NULL,
                   |  experienceId int NOT NULL,
                   |  title varchar(255) NOT NULL,
                   |  startDate int NOT NULL,
                   |  endDate int,
                   |  CONSTRAINT PK_EmploymentPosition PRIMARY KEY (id)
                   |  $fk
                   |);
                 """.stripMargin
            dropIfExists(TABLE_EMPLOYMENT_POSITION)
            SQL(sql).execute()
        }

        def createEmploymentDuty(implicit c: Connection): Boolean = {
            val fk = ""//",CONSTRAINT FK_PositionDuty FOREIGN KEY (positionId) REFERENCES $TABLE_EMPLOYMENT_POSITION(id)"
            val sql =
                s"""
                   |CREATE TABLE $TABLE_EMPLOYMENT_DUTY (
                   |  id int NOT NULL,
                   |  positionId int NOT NULL,
                   |  description varchar(255) NOT NULL,
                   |  CONSTRAINT PK_EmploymentDuty PRIMARY KEY (id)
                   |  $fk
                   |);
                 """.stripMargin
            dropIfExists(TABLE_EMPLOYMENT_DUTY)
            SQL(sql).execute()
        }

    }
}
