package xyz.meggls.megglsxyz.db

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

object DbExecutionContext {

    implicit private val akkaSystem: ActorSystem = ActorSystem.apply()

    val ctx: ExecutionContext = akkaSystem.dispatchers.lookup("db-context")

}
