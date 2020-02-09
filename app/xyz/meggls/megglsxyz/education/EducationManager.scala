package xyz.meggls.megglsxyz.education

import scala.concurrent.Future

class EducationManager {

    import EducationManager._

    def getAllEducation: Future[List[Education]] = {
        Future.successful(InitData.data)
    }

}

object EducationManager {

    private object InitData {

        val data = List(
            Education(
                schoolName = "University of Colorado at Boulder - Leeds School of Business",
                endDate = 20151215,
                programs = List(
                    EducationProgram(
                        programName = "Bachelor of Science",
                        focus = "Business Administration, emphasis in Marketing",
                        secondary = List(
                            "Minor in Economics",
                            "Certificate in Operations and Information Management"
                        )
                    )
                )
            ),
            Education(
                schoolName = "Virginia Polytechnic Institute and State University",
                endDate = 20190512,
                programs = List(
                    EducationProgram(
                        programName = "Master's Degree",
                        focus = "Information Technology, emphasis in Software Engineering",
                        secondary = List(
                            "Additional coursework in networking and security"
                        )
                    ),
                    EducationProgram(
                        programName = "Graduate Certificate",
                        priority = 2,
                        focus = "Software Development",
                        secondary = List.empty
                    )
                )
            )
        )
    }
}
