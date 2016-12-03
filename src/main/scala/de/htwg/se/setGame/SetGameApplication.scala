package de.htwg.se.setGame

object SetGameApplication {
  def main(args: Array[String]): Unit = {
    val controller = Controller()
    Tui(controller)
    Gui(controller)
  }
}