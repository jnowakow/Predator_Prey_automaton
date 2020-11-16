package UI


import Logic.Predator
import LotkaVolterraSolver.Solver
import scalafx.animation.AnimationTimer
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.canvas.Canvas
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color


class EquationsVisualizer extends Pane {
  val mainWindow = new BorderPane()

  var alpha = 1.0
  var beta = 0.1
  var gamma = 1.0
  var delta = 0.075
  var dt = 0.01
  var preyCount = 15
  var predatorCount = 7
  var iterations = 1000
  var chart = createChart(List((0, preyCount)), List((0, predatorCount)))


  def createChart(preys: List[(Double, Double)], predators: List[(Double, Double)]): LineChart[Number, Number] = {
    val xAxis = NumberAxis("Time")
    xAxis.setAutoRanging(true)
    xAxis.forceZeroInRange = false
    val yAxis = NumberAxis("Population")
    yAxis.setAutoRanging(true)

    val toChartData = (xy: (Double, Double)) => XYChart.Data[Number, Number](xy._1, xy._2)

    val series1 = new XYChart.Series[Number, Number] {
      name = "Preys"
      data = preys.map(toChartData)
    }

    val series2 = new XYChart.Series[Number, Number] {
      name = "Predators"
      data = predators.map(toChartData)
    }

    val lineChart = new LineChart[Number, Number](xAxis, yAxis)

    lineChart.getData.add(series1)
    lineChart.getData.add(series2)

    lineChart

  }

  val rightWindow: VBox = new VBox() {
    padding = Insets(5,5,5,5)
    spacing = 10
  }

  rightWindow.children.add(chart)

  val startButton: Button = new Button(){
    text = "Show chart"
    onMouseClicked = (_) => {
      val solver = new Solver
      val (preys, predators) = solver.solveEquation(alpha, beta, gamma, delta, dt, preyCount, predatorCount, iterations)

      rightWindow.children.remove(chart)
      chart = createChart(preys, predators)
      rightWindow.children.add(chart)
    }
  }

  val alphaChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      alpha = util.Try(newValue.toDouble).getOrElse(1.0)
    })
    promptText = s"Alpha (default $alpha)"
  }

  val betaChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      beta = util.Try(newValue.toDouble).getOrElse(0.1)
    })
    promptText = s"Beta (default $beta)"
  }

  val gammaChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      gamma = util.Try(newValue.toDouble).getOrElse(1.0)
    })
    promptText = s"Gamma (default $gamma)"
  }

  val deltaChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      delta = util.Try(newValue.toDouble).getOrElse(0.075)
    })
    promptText = s"Delta (default $delta)"
  }

  val dtChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      dt = util.Try(newValue.toDouble).getOrElse(0.02)
    })
    promptText = s"Dt (default $dt)"
  }

  val preyCountChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      preyCount = util.Try(newValue.toInt).getOrElse(15)
    })
    promptText = s"Preys (default $preyCount)"
  }

  val predatorCountChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      predatorCount = util.Try(newValue.toInt).getOrElse(5)
    })
    promptText = s"Predators (default $predatorCount)"
  }

  val iterationsChange: TextField = new TextField{
    text.onChange( (_,_, newValue) =>{
      iterations = util.Try(newValue.toInt).getOrElse(1000)
    })
    promptText = s"Iterations (default $iterations)"
  }

  private val paramsPanel: VBox = new VBox() {
    padding = Insets(5,5,5,5)
    spacing = 5
    children.addAll(
      alphaChange,
      betaChange,
      gammaChange,
      deltaChange,
      dtChange,
      preyCountChange,
      predatorCountChange,
      iterationsChange,
      startButton
    )
  }

  val centralPane: HBox = new HBox() {
    padding = Insets(5,5,5,5)
    spacing = 10
  }
  centralPane.children.addAll(
    paramsPanel,
    rightWindow
  )

  mainWindow.top = centralPane
  children.add(mainWindow)

}
