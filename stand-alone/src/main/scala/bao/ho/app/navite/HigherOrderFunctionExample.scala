package bao.ho.app.navite

object HigherOrderFunctionExample {
  def f1(i: String): Int = i.length
  def f2: String => Int  = i => i.length
  def f3: String => Int  = _.length

  def g1(i: String)(j: Int): Int = i.length + j
  def g2: String => Int => Int   = i => j => i.length + j

  def k1(f: String => Int, s: String): Int = f(s) + 100
  def k2: (String => Int, String) => Int   = (f, s) => f(s) + 100
  def k3: (String => Int, String) => Int   = _.apply(_) + 100

  def o1(f: (String => Int) => Double, g: String => Int): String = f.apply(g).toString
  def o2: ((String => Int) => Double, String => Int) => String   = (f, g) => f(g).toString
  def o3: ((String => Int) => Double, String => Int) => String   = _.apply(_).toString

  def m1(b: Boolean, param: String, f1: String => Int, f2: String => Int): Int =
    if (b) f1(param)
    else f2(param)

  def k1(params: String, f: String => Int): Int = {
    val validatedParam =
      if (params.length > 10) params.take(10)
      else params
    f(validatedParam)
  }

  def main(args: Array[String]): Unit = {}
}
