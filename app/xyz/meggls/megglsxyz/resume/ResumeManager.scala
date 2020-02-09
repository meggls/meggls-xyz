package xyz.meggls.megglsxyz.resume

import javax.inject.Singleton
import xyz.meggls.megglsxyz.contactInfo.ContactInfo
import xyz.meggls.megglsxyz.education.Education
import xyz.meggls.megglsxyz.employment.EmploymentExperience

@Singleton
class ResumeManager {

    def compileResume(contactInfo: ContactInfo, experience: List[EmploymentExperience], education: List[Education]): Resume = {
        Resume(
            contactInfo,
            experience,
            education
        )
    }

}