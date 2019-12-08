module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import Json.Decode as JD exposing (Decoder, field, string, int, map2, map3)
import Json.Encode as JE

main = Browser.element
  { init = startIt
  , update = updateIt
  , subscriptions = noSubscriptions
  , view = showIt
  }


type alias Person =
  { id: Int,
    nickname: String
  , score: Int
  }

-- type alias EditModel
--     = { person: Maybe Person, msg: String }

-- type alias ListModel
--     = ListModel { people: List Person, msg: String }

-- type Model
--     = EditModel
--     | ListModel

-- We have to declare EditModel and ListModel IN the Model type, since we can only declare then once.
-- The original Main.elm file had a type alias for each, which will not work.
type Model
  = EditModel { person: Maybe Person, msg: String }
  | ListModel { people: List Person, msg: String }

type Msg
  = FetchPeople
--  | FetchPerson
  | PersonPutted (Result Http.Error ())
  | GotPeople (Result Http.Error (List Person))
  | GotPerson (Result Http.Error Person)


startIt: () -> (Model, Cmd Msg)
startIt _ = (ListModel {people = [], msg = "OK"}, Cmd.none)
-- startIt _ = ({ people = [], msg = "OK" }, Cmd.none)

updateIt:  Msg -> Model -> (Model, Cmd Msg)
updateIt message model =
  case model of
    EditModel { person, msg } ->
      case message of
        FetchPeople -> (model, fetchPerson)

        GotPeople _ -> (model, Cmd.none)

        GotPerson _ -> (model, Cmd.none)
        PersonPutted _ -> (model, Cmd.none)
    ListModel { people, msg } ->
      case message of
        FetchPeople -> (model, fetchPeople)

        GotPeople (Ok p) -> (ListModel{ people = p, msg = msg }, Cmd.none)
        GotPeople (Err err) -> (ListModel{ people = people , msg = (printError err) }, Cmd.none)

        GotPerson _ -> (model, Cmd.none)
        PersonPutted _ -> (model, Cmd.none)

printError: Http.Error -> String
printError error =
  case error of
    Http.BadBody m -> "Bad body "++m
    Http.BadUrl u -> "Bad URL: "++u
    Http.Timeout -> "Timeout"
    Http.NetworkError -> "Network panic"
    Http.BadStatus i -> "Bad Status: "++(String.fromInt i)



fetchPerson: Cmd Msg
fetchPerson =
  Http.get
    { url = "gamer.json"
    , expect = Http.expectJson GotPerson personDecoder
    }

fetchPeople: Cmd Msg
fetchPeople =
  Http.request
    { method = "GET"
    , headers = []
    , url = "http://localhost:4711/gamer"
    -- , url = "gamers.json"
    , body = Http.emptyBody
    , expect = Http.expectJson GotPeople personListDecoder
    , timeout = Nothing
    , tracker = Nothing
    }

putPerson: Person -> Cmd Msg
putPerson person =
  Http.request
    { method = "PUT"
    , headers = []
    , url = "..."
    , body = Http.jsonBody (personEncoder person)
    , expect = Http.expectWhatever PersonPutted
    , timeout = Nothing
    , tracker = Nothing
    }

personDecoder: Decoder Person
personDecoder =
  map3 Person
    (field "id" int)
    (field "nickname" string)
    (field "score" int)

personListDecoder: Decoder (List Person)
personListDecoder =
  JD.list personDecoder

personEncoder: Person -> JE.Value
personEncoder person =
  JE.object
    [ ("id", JE.int person.id)
    , ("nickname", JE.string person.nickname)
    , ("score", JE.int person.score)
    ]

noSubscriptions: Model -> Sub Msg
noSubscriptions model =
  Sub.none

showIt: Model -> Html Msg
showIt model =
  case model of  
    ListModel { people, msg } -> div [] ( List.append
      [ text msg
      , hr [] []
      , button [onClick FetchPeople] [text "Click this"]
      , hr [] []
      ]
      (List.map showPerson people)
      )
    _ -> div[] [text "øøøh"]

showPerson: Person -> Html Msg
showPerson person =
  div []
    [ text (String.fromInt person.id)
    , text person.nickname
    , text (String.fromInt person.score)
    ]

getName: Maybe Person -> String
getName mp =
  case mp of
    Just person -> person.nickname++" score: "++(String.fromInt person.score)
    Nothing ->     "Øhh"
