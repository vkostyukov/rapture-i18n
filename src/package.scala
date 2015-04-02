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

object `package` {

  private def context[L <: Language: ClassTag](sc: StringContext, params: List[I18nStringParam[L]]): I18nString[L] =
    new I18nString[L](Map(implicitly[ClassTag[L]] -> sc.parts.zip(params.map(_.i18nString.apply[L]) :+ "").map { case (a, b) => a+b }.mkString))

  implicit class I18nEnrichedStringContext(sc: StringContext) {
    def en(params: I18nStringParam[En]*): I18nString[En] = context[En](sc, params.toList)
    def fr(params: I18nStringParam[Fr]*): I18nString[Fr] = context[Fr](sc, params.toList)
    def de(params: I18nStringParam[De]*): I18nString[De] = context[De](sc, params.toList)
    def it(params: I18nStringParam[It]*): I18nString[It] = context[It](sc, params.toList)
    def es(params: I18nStringParam[Es]*): I18nString[Es] = context[Es](sc, params.toList)
  }
}

