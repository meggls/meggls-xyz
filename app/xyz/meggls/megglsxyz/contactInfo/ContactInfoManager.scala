package xyz.meggls.megglsxyz.contactInfo

import scala.concurrent.Future

class ContactInfoManager {

    import ContactInfoManager._

    def getContactInfo: Future[ContactInfo] = {
        Future.successful(InitData.contactInfo)
    }
}

object ContactInfoManager {

    private object InitData {

        val address = Address(
            line1 = "2201 Lamar St",
            line2 = "Edgewater, CO 80214"
        )

        val contactInfo = ContactInfo(
            name = "Megan Juell",
            address = address,
            phone = "720.403.5418",
            email = "meganljuell@gmail.com"
        )
    }

}