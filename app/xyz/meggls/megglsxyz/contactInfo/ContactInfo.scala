package xyz.meggls.megglsxyz.contactInfo

import play.api.libs.json.{Json, OFormat}

case class ContactInfo (
                         name: String,
                         phone: String,
                         email: String,
                       )

object ContactInfo {
    implicit val format: OFormat[ContactInfo] = Json.format[ContactInfo]
}