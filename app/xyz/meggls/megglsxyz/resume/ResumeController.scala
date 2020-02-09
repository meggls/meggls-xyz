package xyz.meggls.megglsxyz.resume

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import xyz.meggls.megglsxyz.contactInfo.ContactInfoManager
import xyz.meggls.megglsxyz.education.EducationManager
import xyz.meggls.megglsxyz.employment.EmploymentManager
import xyz.meggls.megglsxyz.play.XyzController

import scala.concurrent.ExecutionContext.Implicits.global

class ResumeController @Inject()(
                                  cc: ControllerComponents,
                                  resumeManager: ResumeManager,
                                  contactInfoManager: ContactInfoManager,
                                  employmentManager: EmploymentManager,
                                  educationManager: EducationManager
                                ) extends XyzController(cc) {

    def getResume: Action[AnyContent] = Action.async { implicit request =>
//        (for {
//            contactInfo <- contactInfoManager.getContactInfo
//            experiences <- employmentManager.getAllEmployment
//            education <- educationManager.getAllEducation
//        } yield resumeManager.compileResume(contactInfo, experiences, education)).map { result =>
//            Ok(result)
//        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getResume"))
        ???
    }

    def getContactInfo: Action[AnyContent] = Action.async { implicit request =>
        contactInfoManager.getContactInfo.map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.contactInfo"))
    }

    def getAllEmployment: Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getAllEmployment.map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getAllEmployment"))
    }

    def getEmploymentExperience(experienceId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentExperience(experienceId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentExperience"))
    }

    def getEmploymentPosition(positionId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPosition(positionId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPosition"))
    }

    def getEmploymentDuty(dutyId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentDuty(dutyId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentDuty"))
    }

}
