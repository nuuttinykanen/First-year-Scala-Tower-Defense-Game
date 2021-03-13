import java.io.FileReader
import java.io.FileNotFoundException
import java.io.BufferedReader
import java.io.IOException
import java.io.FileWriter
import java.io.BufferedWriter
import java.io.BufferedReader

object InfoReader {
  /**
   * Write a method that reads a file using a FileReader to read the file
   * and a BufferedReader to read the lines in the file. Use the
   * BufferedReader as a parameter for your FileOperation function.
   *
   * Check for Exceptions in the code. Particularly whether the file was found
   * and whether there was a reading error i.e. IOException.
   *
   * The reader should return a sequence containing each line of the filtered file.
   */
  def readFile(sourceFile: String, fileOperation: BufferedReader => Seq[String]): Seq[String] = {
    var readyText = Seq[String]()
    try{
      val fileIn = new FileReader(sourceFile)
      val linesIn = new BufferedReader(fileIn)

      try{
        readyText = fileOperation(linesIn)
        }

      finally {
        fileIn.close()
        linesIn.close()
      }
    } catch {
      case notFound: FileNotFoundException => println("File not found")
      case e: IOException => println("Reading finished with error")
    }
    readyText
  }


  /**
   * A helper method that shows you how to read a file without applying any
   * filtering.
   */
  def noFilter(lineReader: BufferedReader) = {
    var resList = Seq[String]()
    var oneLine: String = null
    while ({oneLine = lineReader.readLine(); oneLine != null}) {
      resList = resList :+ oneLine
    }
    resList
  }

}
