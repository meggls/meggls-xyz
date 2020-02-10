package xyz.meggls.megglsxyz.employment

import anorm.{Macro, RowParser}
import play.api.libs.json.{Json, OFormat}

case class Employment(
                                 id: Option[Long] = None,
                                 company: String,
                                 startDate: Int,
                                 endDate: Option[Int],
                                 positions: Option[List[EmploymentPosition]]
                               )

object Employment {
    implicit val jsonFormat: OFormat[Employment] = Json.format[Employment]

    def fromDb(positions: Option[List[EmploymentPosition]] = None)(expDb: EmploymentDTO): Employment = {
        Employment(
            Some(expDb.id),
            expDb.company,
            expDb.startDate,
            expDb.endDate,
            positions
        )
    }
}

case class EmploymentDTO(
                        id: Long,
                        company: String,
                        startDate: Int,
                        endDate: Option[Int]
                        )

object EmploymentDTO {
    val sqlFormat: RowParser[EmploymentDTO] = Macro.namedParser[EmploymentDTO]
}

case class EmploymentPosition(
                               id: Option[Long] = None,
                               title: String,
                               startDate: Int,
                               endDate: Option[Int],
                               duties: Option[List[EmploymentDuty]]
                             )

object EmploymentPosition {
    implicit val jsonFormat: OFormat[EmploymentPosition] = Json.format[EmploymentPosition]

    def fromDb(duties: Option[List[EmploymentDuty]] = None)(posDb: EmploymentPositionDTO): EmploymentPosition = {
        EmploymentPosition(
            Some(posDb.id),
            posDb.title,
            posDb.startDate,
            posDb.endDate,
            duties
        )
    }
}

case class EmploymentPositionDTO(
                      id: Long,
                      experienceId: Long,
                      title: String,
                      startDate: Int,
                      endDate: Option[Int]
                      )

object EmploymentPositionDTO {
    implicit val sqlFormat: RowParser[EmploymentPositionDTO] = Macro.namedParser[EmploymentPositionDTO]
}

case class EmploymentDuty(
                           id: Option[Long] = None,
                           description: String,
                           priority: Int
                         )

object EmploymentDuty {
    implicit val format: OFormat[EmploymentDuty] = Json.format[EmploymentDuty]

    def fromDb(dutyDb: EmploymentDutyDTO): EmploymentDuty = {
        EmploymentDuty(
            Some(dutyDb.id),
            dutyDb.description,
            dutyDb.priority
        )
    }
}

case class EmploymentDutyDTO(
                  id: Long,
                  positionId: Long,
                  description: String,
                  priority: Int
                  )

object EmploymentDutyDTO {
    implicit val sqlFormat: RowParser[EmploymentDutyDTO] = Macro.namedParser[EmploymentDutyDTO]
}