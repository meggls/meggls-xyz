package xyz.meggls.megglsxyz.education

import play.api.libs.json.{Json, OFormat}

case class Education(
                      id: Int = 0,
                      schoolName: String,
                      endDate: Int,
                      programs: List[EducationProgram]
                    )

object Education {
    implicit val format: OFormat[Education] = Json.format[Education]
}

case class EducationProgram(
                    id: Int = 0,
                    programName: String,
                    priority: Int = 1,
                    focus: String,
                    secondary: Option[List[String]]
                  )

object EducationProgram {
    implicit val format: OFormat[EducationProgram] = Json.format[EducationProgram]
}