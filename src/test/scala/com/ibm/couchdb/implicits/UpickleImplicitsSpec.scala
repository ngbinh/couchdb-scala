/*
 * Copyright 2015 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.couchdb.implicits

import com.ibm.couchdb.spec.Fixtures
import org.http4s.Status
import org.specs2.mutable._
import org.specs2.specification.AllExpectations

class UpickleImplicitsSpec extends Specification
                                   with AllExpectations
                                   with Fixtures
                                   with UpickleImplicits {

  val map0  = Map[String, String]()
  val json0 = "{}"

  val map1  = Map[String, String]("key1" -> "val1", "key2" -> "val2")
  val json1 = "{\"key1\":\"val1\",\"key2\":\"val2\"}"

  val map2  = Map[String, Int]("key1" -> 1, "key2" -> 2)
  val json2 = "{\"key1\":1,\"key2\":2}"

  val map3  = Map[String, (String, Int)]("key1" -> (("key1", 1)), "key2" -> (("key2", 2)))
  val json3 = "{\"key1\":[\"key1\",1],\"key2\":[\"key2\",2]}"

  val map4  = Map[String, FixPerson]("key1" -> FixPerson("Alice", 25), "key2" -> FixPerson("Bob", 30))
  val json4 = "{\"key1\":{\"name\":\"Alice\",\"age\":25},\"key2\":{\"name\":\"Bob\",\"age\":30}}"

  private def testRoundtrip[T](obj: T)(implicit r: upickle.Reader[T], w: upickle.Writer[T]) = {
    upickle.read[T](upickle.write(obj)) mustEqual obj
  }

  "Custom upickle Reader and Writer instances" >> {

    "Write and read Map[String, T]" >> {
      upickle.write(map0) mustEqual json0
      upickle.write(map1) mustEqual json1
      upickle.write(map2) mustEqual json2
      upickle.write(map3) mustEqual json3
      upickle.write(map4) mustEqual json4
    }

    "Read Map[String, T] from JSON" >> {
      upickle.read[Map[String, String]](json0) mustEqual map0
      upickle.read[Map[String, String]](json1) mustEqual map1
      upickle.read[Map[String, Int]](json2) mustEqual map2
      upickle.read[Map[String, (String, Int)]](json3) mustEqual map3
      upickle.read[Map[String, FixPerson]](json4) mustEqual map4
    }

    "Write and read an Status" >> {
      testRoundtrip[Status](Status.Ok)
    }

  }

}
