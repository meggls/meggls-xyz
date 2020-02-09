package xyz.meggls.megglsxyz.play

import play.api.mvc.{AbstractController, ControllerComponents, Result}
import xyz.meggls.megglsxyz.util.LogUtil

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

abstract class XyzController(cc: ControllerComponents) extends AbstractController(cc) with LogUtil {

    def completeRequest(name: String)(result: Try[Result]): Future[Result] = {
        result match {
            case Success(s) =>
                Future.successful(s)
            case Failure(e) =>
                log.error(s"Failure during $name", e)
                Future.successful(exceptionRecovery(e))
        }
    }

    private def exceptionRecovery(e: Throwable): Result = {
        e match {
            //TODO: expand this
            case _ => InternalServerError(e.getMessage)
        }
    }

}
