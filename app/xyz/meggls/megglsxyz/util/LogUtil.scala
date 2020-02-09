package xyz.meggls.megglsxyz.util

import com.typesafe.scalalogging.Logger

trait LogUtil {

    implicit protected val log: Logger = Logger(this.getClass)

}
