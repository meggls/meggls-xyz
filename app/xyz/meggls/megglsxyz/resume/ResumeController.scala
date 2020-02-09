package xyz.meggls.template

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.Future

class ResumeController(cc: ControllerComponents, templateManager: ResumeManager)
  extends AbstractController(cc) {

    def allExperience: Action[AnyContent] = Action.async{ implicit request =>
        Future.successful(NotImplemented)
    }

    def jobExperience(jobId: Int) = Action.async { implicit request =>
        Future.successful(NotImplemented)
    }

}
