package xyz.meggls.megglsxyz.resume

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import xyz.meggls.megglsxyz.contactInfo.ContactInfoManager
import xyz.meggls.megglsxyz.db.InitDb
import xyz.meggls.megglsxyz.education.EducationManager
import xyz.meggls.megglsxyz.employment.EmploymentManager
import xyz.meggls.megglsxyz.play.XyzController

import scala.concurrent.ExecutionContext.Implicits.global

class ResumeController @Inject()(
                                  cc: ControllerComponents,
                                  resumeManager: ResumeManager,
                                  contactInfoManager: ContactInfoManager,
                                  employmentManager: EmploymentManager,
                                  educationManager: EducationManager,
                                  initDb: InitDb
                                ) extends XyzController(cc) {

    def resetResume: Action[AnyContent] = Action.async { implicit request =>
        initDb.resetDb.map{ _ => Ok}
          .transformWith(completeRequest(s"${getClass.getSimpleName}.resetResume"))
    }

    def getResume: Action[AnyContent] = Action.async { implicit request =>
        (for {
            contactInfo <- contactInfoManager.getContactInfo
            experiences <- employmentManager.getAllEmployment
            education <- educationManager.getAllEducation
        } yield resumeManager.compileResume(contactInfo, experiences, education)).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getResume"))
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

    def getEmployment(employmentId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentExperience(employmentId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentExperience"))
    }

    def getEmploymentPositions(employmentId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPositions(employmentId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPosition"))
    }

    def getEmploymentPosition(employmentId: Long, positionId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPosition(employmentId, positionId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPosition"))
    }

    def getEmploymentPositionDuties(employmentId: Long, positionId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPositionDuties(employmentId, positionId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentDuty"))
    }

    def getEmploymentPositionDuty(employmentId: Long, positionId: Long, dutyId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPositionDuty(employmentId, positionId, dutyId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentDuty"))
    }

}
