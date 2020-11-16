package LotkaVolterraSolver

import Logic.Predator

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

class Solver {
  def solveEquation(alpha: Double, beta: Double, gamma: Double, delta: Double,
                    dt: Double, preyCount: Int, predatorCount: Int,
                    iterationsCount: Int): (List[(Double, Double)], List[(Double, Double)]) = {

    def dx(x: Double, y: Double): Double = {
      val dx_dt = x * (alpha - beta * y)
      dx_dt * dt
    }

    def dy(x: Double, y: Double): Double = {
      val dy_dt = y * (delta * x - gamma)
      dy_dt * dt
    }


    @tailrec
    def compute(preyCount: Double, predatorCount: Double,
                actualPreyHistory: List[(Double, Double)], actualPredatorHistory: List[(Double, Double)],
                iteration: Int): (List[(Double, Double)], List[(Double, Double)]) = {

      if(iteration > 0){
        val dx_1 = dx(preyCount, predatorCount)
        val dy_1 = dy(preyCount, predatorCount)
        val dx_2 = dx(preyCount + dx_1, predatorCount + dy_1)
        val dy_2 = dy(preyCount + dx_1, predatorCount + dy_1)

        val newPreyCount = preyCount + (dx_1 + dx_2)/2
        val newPredatorCount = predatorCount + (dy_1 + dy_2)/2
        val newTime = actualPreyHistory.head._1 + dt

        compute(newPreyCount, newPredatorCount,
          (newTime, newPreyCount) :: actualPreyHistory, (newTime, newPredatorCount) :: actualPredatorHistory,
          iteration -1 )
      }
      else{
        (actualPreyHistory.reverse, actualPredatorHistory.reverse)
      }

    }

    compute(preyCount, predatorCount, List((0, preyCount)), List((0, predatorCount)), iterationsCount)
  }

}
