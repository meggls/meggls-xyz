package xyz.meggls.megglsxyz.contactInfo

import play.api.libs.json.{Json, OFormat}

case class ContactInfo (
                         name: String,
                         address: Address,
                         phone: String,
                         email: String,
                       )

object ContactInfo {
    implicit val format: OFormat[ContactInfo] = Json.format[ContactInfo]
}

case class Address (
                     line1: String,
                     line2: String
                   )

object Address {
    implicit val format: OFormat[Address] = Json.format[Address]
}