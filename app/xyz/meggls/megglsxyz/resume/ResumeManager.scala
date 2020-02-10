package xyz.meggls.megglsxyz.resume

import javax.inject.Singleton
import xyz.meggls.megglsxyz.contactInfo.ContactInfo
import xyz.meggls.megglsxyz.education.Education
import xyz.meggls.megglsxyz.employment.Employment

@Singleton
class ResumeManager {

    def compileResume(contactInfo: ContactInfo, experience: List[Employment], education: List[Education]): Resume = {
        Resume(
            contactInfo,
            experience,
            education
        )
    }

}