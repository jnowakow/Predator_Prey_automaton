package Logic

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


class Simulation(val params: SimulationParams) {
  private val worldMap = new mutable.HashMap[Point, Animal]()
  private val rand = scala.util.Random
  private val lowerLeft = Point(0,0)
  private val upperRight = Point(params.mapWidth, params.mapHeight)


  private def freeRandomPosition(): Point ={
    var point = Point(rand.nextInt(params.mapWidth), rand.nextInt(params.mapHeight))

    while (worldMap.contains(point)) {
      point = Point(rand.nextInt(params.mapWidth), rand.nextInt(params.mapHeight))
    }
    point
  }


  private def setUpSimulation(): Unit = {

    val size = params.mapHeight * params.mapWidth
    val preyCount = (size * params.initialPreyPercentage).toInt
    val predatorCount = (size * params.initialPredatorPercentage).toInt

    for (_ <- 1 to preyCount){
      val point = freeRandomPosition()
      worldMap(point) = Prey(point)
    }

    for (_ <- 1 to predatorCount){
      val point = freeRandomPosition()
      worldMap(point) = Predator(point)
    }

  }

  setUpSimulation()

  def inMap(point: Point): Boolean = point.precedes(upperRight) && point.follows(lowerLeft)

  def findNearbyPrey(position: Point): Option[Point] = {

    for(i <- -1 to 1;
        j <- -1 to 1
        if i != 0 || j != 0){
      val tryPosition = position.add(new Point(i, j))
      if(inMap(tryPosition)){
        val animal = worldMap.getOrElse(tryPosition, None)

        animal match {
          case Prey(_) =>
            return Some(tryPosition)
          case other => Unit
        }
      }
    }
    None
  }

  def findSafePosition(position: Point): Option[Point] = {
    var freePosition = position

    for(i <- -1 to 1;
        j <- -1 to 1
        if i != 0 || j != 0) {

      val tryPosition = position.add(new Point(i, j))
      if(inMap(tryPosition)) {
        val animal = worldMap.getOrElse(tryPosition, None)

        animal match {
          case Predator(_) => return None
          case None => freePosition = tryPosition
          case Prey(_) => Unit
        }
      }
    }

    if (freePosition != position) {
      Some(freePosition)
    }
    else {
      None
    }
  }


  def findFreePositions(position: Point): Option[ListBuffer[Point]] = {
    val freePositions = new ListBuffer[Point]

    for(i <- -1 to 1;
        j <- -1 to 1
        if i != 0 || j != 0) {

        val tryPosition = position.add(new Point(i, j))
        if (!worldMap.contains(tryPosition) && inMap(tryPosition) ){
          freePositions.append(tryPosition)
        }
    }

    if(freePositions.nonEmpty) {
      Some(freePositions)
    } else {
      None
    }
  }

  def resultLists(): (List[Animal], List[Animal]) ={
    val preys = new ListBuffer[Animal]
    val predators = new ListBuffer[Animal]

    worldMap.values.foreach {
      case animal@Prey(_) => preys.append(animal)
      case animal@Predator(_) => predators.append(animal)
    }

    (predators.toList, preys.toList)
  }

  def nextState(): (List[Animal], List[Animal]) = {

    worldMap.values.foreach {
      case animal@Predator(_)
      =>
        if (rand.nextDouble() > params.predatorDeathProbability) {
          findNearbyPrey(animal.position) match {
            case Some(point) =>
              worldMap.remove(point)
              worldMap(point) = Predator(point)
            case None =>
              worldMap.remove(animal.position)
          }
        } else {
          worldMap.remove(animal.position)
        }
      case animal@Prey(_) => Unit

    }

    worldMap.values.foreach {
      case Prey(position) =>
          if(rand.nextDouble() < params.preyBirthProbability){
            findSafePosition(position) match {
              case Some(safePosition) => worldMap(safePosition) = Prey(safePosition)
              case None => Unit
            }
          }
      case other => Unit
    }


    worldMap.values.foreach{
      animal =>
        findFreePositions(animal.position) match {
          case Some(list) =>
            worldMap.remove(animal.position)
            animal.move(list)
            worldMap(animal.position) = animal
          case None => Unit
        }

    }

    resultLists()
  }



}