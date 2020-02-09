package xyz.meggls.megglsxyz.resume.models

case class Resume(
                 contactInfo: ContactInfo,
                 experiences: List[Experience],
                 educations: List[Education]
                 )

case class ContactInfo (
                         name: String,
                         address: Address,
                         phone: String,
                         email: String,
                       )

case class Address (
                   line1: String,
                   line2: String
                   )