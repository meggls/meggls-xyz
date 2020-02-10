package xyz.meggls.megglsxyz.resume

import play.api.libs.json.{Json, OFormat}
import xyz.meggls.megglsxyz.contactInfo.ContactInfo
import xyz.meggls.megglsxyz.education.Education
import xyz.meggls.megglsxyz.employment.Employment

case class Resume(
                   contactInfo: ContactInfo,
                   employment: List[Employment],
                   education: List[Education]
                 )

object Resume {
    implicit val format: OFormat[Resume] = Json.format[Resume]
}