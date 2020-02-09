package xyz.meggls.megglsxyz.experience

class ExperienceManager {



}

object ExperienceManager {

    object InitData {
        val data: List[Experience] = List(
            Experience(
                company = ""
            )
            Experience(
                company = "Rally Software Development",
                startDate = 20141101,
                endDate = Some(20150501),
                positions = List(
                    Position(
                        title = "Marketing Performance and Operations Intern",
                        startDate = 20141101,
                        endDate = Some(20150501),
                        duties = List(
                            "Monitored incoming data from Salesforce.com and Eloqua to find and resolve automation errors and data entry errors",
                            "Generated reports through the Salesforce.com GUI and MySQL databases",
                            "Configured discount codes for the eCommerce system through Commerce Kickstart",
                            "Scheduled presentations and managed recordings for weekly meetings through WebEx and Google Hangouts",
                            "Processed contracts and purchase orders for the Marketing department through NetSuite, Docusign, and email"
                        )
                    )
                )
            )
        )
    }

}
