package lt.viko.eif.dbchecker;
/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoterRepositoryTests {

    @Autowired
    VoterRepository repository;

    Voter dave, oliver, carter;

    @Before
    public void setUp() {

        repository.deleteAll();

        dave = repository.save(new Voter("Dave", "Matthews"));
        oliver = repository.save(new Voter("Oliver August", "Matthews"));
        carter = repository.save(new Voter("Carter", "Beauford"));
    }

    @Test
    public void setsIdOnSave() {

        Voter dave = repository.save(new Voter("Dave", "Matthews"));

        assertThat(dave.id).isNotNull();
    }

    @Test
    public void findsByLastName() {

        List<Voter> result = repository.findByLastName("Beauford");

        Assertions.assertThat(result).hasSize(1).extracting("firstName").contains("Carter");
    }

    @Test
    public void findsByExample() {

        Voter probe = new Voter(null, "Matthews");

        List<Voter> result = repository.findAll(Example.of(probe));

        Assertions
            .assertThat(result).hasSize(2).extracting("firstName").contains("Dave", "Oliver August");
    }
}