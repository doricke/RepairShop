
class ClassC:

  def init ( self ):
    print "ClassC method init called"
    self.name = "ClassC.name"

  def __init__ ( self ):
    print "ClassC method __init__ called"
    self.init ()

  def method1 ( self ):
    print "ClassC method1 called"


if ( __name__ == "__main__" ):
  test1 = ClassC ()
  test1.init ()
  test1.method1 ()
  print "ClassC attribute name = ", test1.name

