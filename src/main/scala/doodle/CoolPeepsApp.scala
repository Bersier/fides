package doodle

trait CoolPeepsApp {

  trait Process
  trait Page

  val homepage: Page

  trait CalendarView extends Page {
    type Calendar
    val calendar: Calendar
  }

  trait SelfProfile extends Page
  trait OtherProfile extends Page
  trait MatchingPage extends Page



  trait SignUp extends Process

  // Minimize retaliation possibility
  // Need penalty for not showing up
  // Can user see/choose probability of success?
}
