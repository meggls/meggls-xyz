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

        val contactInfo = ContactInfo(
            name = "Megan Juell",
            phone = "720.403.5418",
            email = "meganljuell@gmail.com"
        )
    }

}