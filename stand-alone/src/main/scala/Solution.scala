object Solution {
  case class Data(v: String) extends Product
  case class A(v: Data, m: Int)

  def main(args: Array[String]): Unit = {
//    val a: Option[A] = Some(A(Data("10"), 20))
//    val x            = a.fold(0)(f => solution(f.v))
//    val y            = a.fold(0)((_.v).andThen().andThen(solution))
//    println(x)
//    solution.an
  }
//    val data = Array(1, 2, 3, 4, 6, 7, 4)
//    println(solution((data.map(_.toInt))))
//    println((args).andThen(solution))

  def solution: Data => Int = d => d.v.toInt
}
