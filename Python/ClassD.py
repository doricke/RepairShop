
from ClassC import ClassC

class ClassD ( ClassC ):

  def __init__ ( self ):
    print "ClassD method __init__ called"
    self.init ()
    print "ClassD self.name = ", self.name
    self.name = "ClassD.name"

  def method2 ( self ):
    print "ClassD method2 called"


if ( __name__ == "__main__" ):
  test1 = ClassC ()
  test1.method1 ()
  print "ClassC attribute name = ", test1.name
  print ""
  test2 = ClassD ()
  test2.method1 ()
  test2.method2 ()
  print "ClassD attribute name = ", test2.name
  print ""
  print "ClassC attribute name = ", test1.name

