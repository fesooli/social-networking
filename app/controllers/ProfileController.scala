package controllers

import javax.inject._
import model.{Profile, ProfileConnection, ProfileSuggestion}
import play.api.libs.json.Json._
import play.api.mvc._
import services.ProfileService

@Singleton
class ProfileController @Inject()(cc: ControllerComponents)(profileService: ProfileService) extends AbstractController(cc) {

  def index = Action {
    Ok(toJson(profileService.getProfiles()))
  }

  def addProfile = Action(parse.json) { implicit request =>
    val profile = request.body.as[Profile]
    profileService.addProfile(profile)
    Ok(toJson(profileService.getProfiles()))
  }

  def connectProfiles= Action(parse.json) { implicit request =>
    val profileConnection = request.body.as[ProfileConnection]
    profileService.connectProfiles(profileConnection)
    Ok(toJson(profileService.getProfiles()))
  }

  def generateConnectionsSuggestions = Action(parse.json) { implicit request =>
    val profileSuggestion = request.body.as[ProfileSuggestion]
    val suggestions = profileService.generateConnectionsSuggestions(profileSuggestion)
    Ok(toJson(profileService.findProfilesByIds(suggestions)))
  }

}
