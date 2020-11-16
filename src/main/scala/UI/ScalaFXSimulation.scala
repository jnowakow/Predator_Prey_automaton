package UI

import java.beans.EventHandler

import Logic.SimulationParams
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField, TextFormatter}
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.paint.Color.Red
import scalafx.scene.text.Text


object ScalaFXSimulation extends JFXApp {


  stage = new PrimaryStage {
    title = "Lotka-Volterra Simulator"

    var mapWidth = 100
    var mapHeight = 100
    var preyPerc = 0.1
    var predatorPerc = 0.05
    var preyBirthProb = 0.9
    var predatorDeathProb = 0.1


    scene = new Scene  {
      val pane = new BorderPane()

      pane.padding = Insets(top = 5, bottom = 5, left = 5, right = 5)
      val text1: Text = new Text {
        text = "Welcome to Predator and Prey simulation"
        fill = Red
        style = "-fx-font: normal bold 15pt sans-serif"
      }

      val text2: Text = new Text {
        text = "Choose the mode of simulation"
        fill = Red
        style = "-fx-font: normal bold 15pt sans-serif"
      }



      pane.top = new VBox(){
        alignment = Center
        children.addAll(
          text1,
          text2
        )
      }

      val simulationWidth: TextField = new TextField{
        text.onChange( (_,_, newValue) =>{
          mapWidth = util.Try(newValue.toInt).getOrElse(100)
        })
        promptText = "Map width (default 100)"
      }

      val simulationHeight: TextField = new TextField{
        text.onChange( (_,_, newValue) =>{
          mapHeight = util.Try(newValue.toInt).getOrElse(100)
        })
        promptText = "Map height (default 100)"
      }

      val preyPercentage: TextField = new TextField{
        text.onChange( (_,_, newValue) =>{
          preyPerc = util.Try(newValue.toDouble).getOrElse(0.1)
        })
        promptText = "Prey initial percentage (default 0.1)"
      }

      val predatorPercentage: TextField = new TextField{
        text.onChange( (_,_, newValue) =>{
          predatorPerc = util.Try(newValue.toDouble).getOrElse(0.05)
        })
        promptText = "Predators initial percentage (default 0.05)"
      }

      val preyBirthProbability: TextField = new TextField{
        text.onChange( (_,_, newValue) =>{
          preyBirthProb = util.Try(newValue.toDouble).getOrElse(0.9)
        })
        promptText = "Preys' birth probability (default 0.9)"
      }

      val predatorDeathProbability: TextField = new TextField{
        text.onChange( (_,_, newValue) =>{
          predatorDeathProb = util.Try(newValue.toDouble).getOrElse(10.0)
        })
        promptText = "Predators' death probability (default 0.1)"
      }

      pane.center = new VBox(){
        padding =  Insets(5, 5, 5 ,5)
        spacing = 10
        alignment = Center
        children.addAll(
          simulationWidth,
          simulationHeight,
          predatorPercentage,
          preyPercentage,
          preyBirthProbability,
          predatorDeathProbability
        )
      }


      val cellAutomata: Button = new Button("Start cell automata"){
        onMouseClicked = (_) => {
          val params = new SimulationParams(mapWidth, mapHeight, preyPerc, predatorPerc, preyBirthProb, predatorDeathProb )
          root = new CellAutomataVisualizer(params)
          stage.sizeToScene()

        }
      }

      val differentialEquations: Button = new Button("Start differential equations"){
        onMouseClicked = (_) => {
          root = new EquationsVisualizer
          stage.minHeight = 450
          stage.minWidth = 700
        }
      }


      pane.bottom = new VBox(){
        padding =  Insets(5, 5, 5 ,5)
        spacing = 10
        alignment = Center
        children.addAll(
          cellAutomata,
          differentialEquations
        )
      }


      root = pane
    }

  }

}
