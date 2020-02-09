package xyz.meggls.megglsxyz.resume.models

case class Education (
                     schoolName: String,
                     endDate: Int,
                     programs: List[Program]
                     )

case class Program (
                   programName: String,
                   focus: String,
                   secondary: List[String]
                   )