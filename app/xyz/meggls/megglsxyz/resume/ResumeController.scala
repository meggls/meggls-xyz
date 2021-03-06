package xyz.meggls.megglsxyz.resume

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import xyz.meggls.megglsxyz.contactInfo.ContactInfoManager
import xyz.meggls.megglsxyz.db.InitDb
import xyz.meggls.megglsxyz.education.{Education, EducationManager, EducationProgram}
import xyz.meggls.megglsxyz.employment.{Employment, EmploymentDuty, EmploymentManager, EmploymentPosition}
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
        employmentManager.getEmployment(employmentId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmployment"))
    }

    def updateEmployment(employmentId: Long): Action[Employment] = Action(parse.json[Employment]).async { implicit request =>
        employmentManager.updateEmployment(employmentId, request.body).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.updateEmployment"))
    }

    def deleteEmployment(employmentId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.deleteEmployment(employmentId).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.deleteEmployment"))
    }

    def addEmployment: Action[Employment] = Action.async(parse.json[Employment]) { implicit request =>
        employmentManager.addEmployment(request.body).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.addEmployment"))
    }

    def getEmploymentPositions(employmentId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPositions(employmentId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPositions"))
    }

    def getEmploymentPosition(employmentId: Long, positionId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPosition(employmentId, positionId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPosition"))
    }

    def updateEmploymentPosition(employmentId: Long, positionId: Long): Action[EmploymentPosition] = Action(parse.json[EmploymentPosition]).async { implicit request =>
        employmentManager.updateEmploymentPosition(employmentId, positionId, request.body).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.updateEmploymentPosition"))
    }

    def deleteEmploymentPosition(employmentId: Long, positionId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.deleteEmploymentPosition(employmentId, positionId).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.deleteEmploymentPosition"))
    }

    def addEmploymentPosition(employmentId: Long) = Action.async(parse.json[EmploymentPosition]) { implicit request =>
        employmentManager.addEmploymentPosition(employmentId, request.body).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.addEmploymentPosition"))
    }

    def getEmploymentPositionDuties(employmentId: Long, positionId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPositionDuties(employmentId, positionId).map { result =>
            Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPositionDuties"))
    }

    def getEmploymentPositionDuty(employmentId: Long, positionId: Long, dutyId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.getEmploymentPositionDuty(employmentId, positionId, dutyId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEmploymentPositionDuty"))
    }

    def updateEmploymentPositionDuty(employmentId: Long, positionId: Long, dutyId: Long): Action[EmploymentDuty] = Action(parse.json[EmploymentDuty]).async { implicit request =>
        employmentManager.updateEmploymentDuty(employmentId, positionId, dutyId, request.body).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.updateEmploymentPositionDuty"))
    }

    def deleteEmploymentPositionDuty(employmentId: Long, positionId: Long, dutyId: Long): Action[AnyContent] = Action.async { implicit request =>
        employmentManager.deleteEmploymentDuty(employmentId, positionId, dutyId).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.deleteEmploymentPositionDuty"))
    }

    def addEmploymentPositionDuty(employmentId: Long, positionId: Long) = Action.async(parse.json[EmploymentDuty]) { implicit request =>
        employmentManager.addEmploymentDuty(employmentId, positionId, request.body).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.addEmploymentPositionDuty"))
    }

    def getAllEducation = Action.async { implicit request =>
        educationManager.getAllEducation.map {
            request => Ok(Json.toJson(request))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getAllEducation"))
    }

    def getEducation(educationId: Long) = Action.async { implicit request =>
        educationManager.getEducation(educationId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEducation"))
    }

    def updateEducation(educationId: Long) = Action.async(parse.json[Education]){ implicit request =>
        educationManager.updateEducation(educationId, request.body).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.updateEducation"))
    }

    def deleteEducation(educationId: Long) = Action.async{ implicit request =>
        educationManager.deleteEducation(educationId).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.updateEducation"))
    }

    def addEducation = Action.async(parse.json[Education]){implicit request =>
        educationManager.addEducation(request.body).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.addEducation"))
    }

    def getEducationPrograms(educationId: Long) = Action.async{implicit request =>
        educationManager.getEducationPrograms(educationId).map {
            result => Ok(Json.toJson(result))
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEducationPrograms"))
    }

    def getEducationProgram(educationId: Long, programId: Long) = Action.async{implicit request =>
        educationManager.getEducationProgram(educationId, programId).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.getEducationProgram"))
    }

    def updateEducationProgram(educationId: Long, programId: Long) = Action.async(parse.json[EducationProgram]){ implicit request =>
        educationManager.updateEducationProgram(educationId, programId, request.body).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.updateEducationProgram"))
    }

    def deleteEducationProgram(educationId: Long, programId: Long) = Action.async { implicit request =>
        educationManager.deleteEducationProgram(educationId, programId).map {
            case 0 => NotFound
            case _ => Ok
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.deleteEducationProgram"))
    }

    def addEducationProgram(educationId: Long) = Action.async(parse.json[EducationProgram]){ implicit request =>
        educationManager.addEducationProgram(educationId, request.body).map {
            case Some(result) => Ok(Json.toJson(result))
            case None => NotFound
        }.transformWith(completeRequest(s"${getClass.getSimpleName}.addEducationProgram"))
    }

}
