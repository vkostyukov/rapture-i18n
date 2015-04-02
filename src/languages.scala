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

import scala.reflect._
import annotation.unchecked._

object languages {
  object en { implicit val defaultLanguage: DefaultLanguage[En] = DefaultLanguage[En](implicitly[ClassTag[En]]) }
  object fr { implicit val defaultLanguage: DefaultLanguage[Fr] = DefaultLanguage[Fr](implicitly[ClassTag[Fr]]) }
  object de { implicit val defaultLanguage: DefaultLanguage[De] = DefaultLanguage[De](implicitly[ClassTag[De]]) }
  object it { implicit val defaultLanguage: DefaultLanguage[It] = DefaultLanguage[It](implicitly[ClassTag[It]]) }
  object es { implicit val defaultLanguage: DefaultLanguage[Es] = DefaultLanguage[Es](implicitly[ClassTag[Es]]) }
}

case class DefaultLanguage[-L <: Language](tag: ClassTag[L @uncheckedVariance])

trait Language
final class En extends Language
final class Fr extends Language
final class De extends Language
final class It extends Language
final class Es extends Language
// Include other two-letter ISO codes
