package xyz.meggls.megglsxyz.experience

case class Experience(
                       company: String,
                       startDate: Int,
                       endDate: Option[Int],
                       positions: List[Position]
                     )

case class Position(
                     title: String,
                     startDate: Int,
                     endDate: Option[Int],
                     duties: List[String]
                   )
