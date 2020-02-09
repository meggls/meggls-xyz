package xyz.meggls.megglsxyz.employment

import anorm.{Macro, RowParser}
import play.api.libs.json.{Json, OFormat}

case class EmploymentExperience(
                                 id: Long = 0,
                                 company: String,
                                 startDate: Int,
                                 endDate: Option[Int],
                                 positions: List[EmploymentPosition]
                               )

object EmploymentExperience {
    implicit val jsonFormat: OFormat[EmploymentExperience] = Json.format[EmploymentExperience]
}

case class ExperienceDb (
                        id: Long,
                        company: String,
                        startDate: Int,
                        endDate: Option[Int]
                        )

object ExperienceDb {
    implicit val sqlFormat: RowParser[ExperienceDb] = Macro.namedParser[ExperienceDb]
}

case class EmploymentPosition(
                               id: Long = 0,
                               title: String,
                               startDate: Int,
                               endDate: Option[Int],
                               duties: List[EmploymentDuty]
                             )

object EmploymentPosition {
    implicit val jsonFormat: OFormat[EmploymentPosition] = Json.format[EmploymentPosition]
}

case class PositionDb (
                      id: Long,
                      experienceId: Long,
                      title: String,
                      startDate: Int,
                      endDate: Option[Int]
                      )

object PositionDb {
    implicit val sqlFormat: RowParser[PositionDb] = Macro.namedParser[PositionDb]
}

case class EmploymentDuty(
                           id: Long = 0,
                           description: String,
                           priority: Int
                         )

object EmploymentDuty {
    implicit val format: OFormat[EmploymentDuty] = Json.format[EmploymentDuty]
}

case class DutyDb (
                  id: Long,
                  positionId: Long,
                  description: String,
                  priority: Int
                  )

object DutyDb {
    implicit val sqlFormat: RowParser[DutyDb] = Macro.namedParser[DutyDb]
}