/******************************************************************************************************************\
* Rapture I18N, version 1.2.0. Copyright 2010-2015 Jon Pretty, Propensive Ltd.                                     *
*                                                                                                                  *
* The primary distribution site is http://rapture.io/                                                              *
*                                                                                                                  *
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in complance    *
* with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.            *
*                                                                                                                  *
* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed *
* on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License    *
* for the specific language governing permissions and limitations under the License.                               *
\******************************************************************************************************************/
package rapture.i18n

import scala.reflect.ClassTag

import language.implicitConversions

object I18nString {
  implicit def convertToString[L <: Language, L2 >: L <: Language: DefaultLanguage](i18nString: I18nString[L]): String =
    i18nString.map(implicitly[DefaultLanguage[L2]].tag)
}

class I18nString[+Langs <: Language](private val map: Map[ClassTag[_], String]) {
  def apply[Lang >: Langs](implicit ct: ClassTag[Lang]): String = map(ct)
  def |[Lang2 <: Language](s2: I18nString[Lang2]): I18nString[Langs with Lang2] =
    new I18nString[Langs with Lang2](map ++ s2.map)

  override def toString = {
    val langs = map.keys.map(_.runtimeClass.getName.split("\\.").last.toLowerCase).mkString("|")
    val content = "\""+map.get(implicitly[ClassTag[En]]).orElse(map.headOption.map(_._1)).getOrElse("")+"\""
    s"$langs:$content"
  }

  override def hashCode = map.hashCode ^ 248327829
  override def equals(that: Any) = that match {
    case that: I18nString[_] => map == that.map
    case _ => false
  }
}

object I18nStringParam {
  implicit def stringToStringParam[L <: Language](s: String): I18nStringParam[L] =
    new I18nStringParam[L](new I18nString[L](Map()) {
      override def apply[Lang >: L](implicit ct: ClassTag[Lang]) = s
    })
  
  implicit def toI18nStringParam[T, L <: Language](t: T)(implicit is: I18nStringable[L, T]): I18nStringParam[L] =
    I18nStringParam(is.make(t))
}
case class I18nStringParam[+L <: Language](i18nString: I18nString[L]) extends AnyVal

trait I18nStringable[+L <: Language, T] { def make(t: T): I18nString[L] }

object I18nStringable {
  implicit def i18nStringI18nStringable[L <: Language]: I18nStringable[L, I18nString[L]] =
    new I18nStringable[L, I18nString[L]] {
      def make(t: I18nString[L]): I18nString[L] = t
    }
}

trait LanguageBundle[Langs <: Language] {
  type IString = I18nString[Langs]
}

