
class ClassA:

  def init ( self ):
    print "ClassA method init called"
    self.name = "ClassA.name"

  def __init__ ( self ):
    print "ClassA method __init__ called"
    self.init ()

  def method1 ( self ):
    print "ClassA method1 called"

  def method3 ( self ):
    print "ClassA method3 called"
    print "ClassA self.name = ", self.name

class ClassB ( ClassA ):

  def __init__ ( self ):
    print "ClassB method __init__ called"
    ClassA.__init__ ( self )
    # self.init ()
    print "ClassB self.name = ", self.name
    self.name = "ClassB.name"

  def method2 ( self ):
    print "ClassB method2 called"

  def method3 ( self ):
    print "ClassB method3 called"
    ClassA.method3 ( self )
    print "ClassB self.name = ", self.name


if ( __name__ == "__main__" ):
  test1 = ClassA ()
  test1.method1 ()
  print "ClassA attribute name = ", test1.name
  print ""
  test2 = ClassB ()
  test2.method1 ()
  test2.method2 ()
  print "ClassB attribute name = ", test2.name
  print ""
  test2.method3 ()

