package model

import play.api.libs.json._

import scala.collection.mutable.ListBuffer

case class Profile(id: String, name: String, age: Int, uf: String, hiddenFromConnectionsSuggestions: Boolean, connections: ListBuffer[String])
object Profile {
  implicit val reads = Json.reads[Profile]
  implicit val writes = Json.writes[Profile]
  implicit val format = Json.format[Profile]
}

case class ProfileConnection(idProfileOne: String, idProfileTwo: String)
object ProfileConnection {
  implicit val reads = Json.reads[ProfileConnection]
  implicit val writes = Json.writes[ProfileConnection]
  implicit val format = Json.format[ProfileConnection]
}

case class ProfileSuggestion(idProfile: String)
object ProfileSuggestion {
  implicit val reads = Json.reads[ProfileSuggestion]
  implicit val writes = Json.writes[ProfileSuggestion]
  implicit val format = Json.format[ProfileSuggestion]
}