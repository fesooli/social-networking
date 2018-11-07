package services

import java.util.UUID.randomUUID

import model.{Profile, ProfileConnection, ProfileSuggestion}

import scala.collection.mutable
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer

class ProfileService {

  var profiles: ListBuffer[Profile] = ListBuffer[Profile]()

  def getProfiles(): ListBuffer[Profile] = profiles

  def addProfile(profile: Profile) = {
    var newProfile = new Profile(randomUUID().toString,
      profile.name,
      profile.age,
      profile.uf,
      profile.hiddenFromConnectionsSuggestions,
      profile.connections)
    profiles.:+=(newProfile)
    profiles
  }

  def connectProfiles(profileConnection: ProfileConnection) = {
    profiles.map(profile => {
      if (profile.id == profileConnection.idProfileOne) {
        connect(profile, profileConnection.idProfileTwo)
      } else if (profile.id == profileConnection.idProfileTwo) {
        connect(profile, profileConnection.idProfileOne)
      }
    })
  }

  def generateConnectionsSuggestions(profileSuggestion: ProfileSuggestion) = {
    var profileConnectionsSuggestions = ListBuffer[String]()

    val profile = profiles.find(profile => profile.id.equals(profileSuggestion.idProfile)).get // busquei o perfil passado
    val profilesConnections = profile.connections // peguei as conexoes dele

    if (!profilesConnections.isEmpty) {
      print(profilesConnections)

      profilesConnections.foreach(p => { // pra cada conexao passada vou iterar
        profileConnectionsSuggestions ++=
          profiles
            .filter(pr => pr.id.equals(p))
            .flatMap(pro => pro.connections) // retorno só os ids dos perfis
            .filter(friendsProfile =>
            !profile.connections.contains(friendsProfile)
              && !friendsProfile.contains(profile.id)) // verifico se ja nao tenho essa conexao como amigo
            .distinct // tiro os repetidos
      })

      profileConnectionsSuggestions = profileConnectionsSuggestions.distinct
      print(profileConnectionsSuggestions)
      val mapConnectionsWeight = Map[String, Int]()

      // verificar a lista de sugestoes quem tem mais amigos em comum com meus amigos
      // a key do map é a sugestão e o value o peso (qtde de amigos em comum)
      profileConnectionsSuggestions.foreach(p => {
        profiles
          .filter(profileInList => profileInList.id.equals(p) && !profileInList.hiddenFromConnectionsSuggestions)
          .foreach(profileInList => {
            profilesConnections.foreach(connection => {
              if (profileInList.connections.contains(connection)) {
                if (!mapConnectionsWeight.contains(p)) {
                  mapConnectionsWeight += (p -> 1)
                } else {
                  mapConnectionsWeight(p) += 1
                }
              }
            })
          })
      })

      // faz o sort do map de acordo com os pesos, e retorna uma lista ordenada só com os ids
      mapConnectionsWeight.toList.sortWith((x, y) => x._2 > y._2).toMap.keySet.toList
    } else {
      profiles
        .filter(profileInList =>
          !profileInList.hiddenFromConnectionsSuggestions
            && !profileInList.id.equals(profileSuggestion.idProfile))
        .filter(profileInList => profileInList.uf.equals(profile.uf))
        .map(profileInList => profileInList.id)
        .toList
    }
  }

  def findProfilesByIds(idsList: List[String]): List[Profile] = {
    idsList.flatMap(id => {
      profiles.find(profile => profile.id.equals(id)).toList
    })
  }

  def connect(profile: Profile, id: String): Unit = {
    profile.connections += id
  }

}
