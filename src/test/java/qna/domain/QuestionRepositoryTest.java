package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    private User user;

    @BeforeEach
    void init() {
        user = userRepository.save(new User("yulmucha", "password", "Yul", "yul@google.com"));
    }

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        Question expected = new Question(user, "제목1", "내용1");
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void identity() {
        Question q1 = questionRepository.save(new Question(user, "제목1", "내용1"));
        Question q2 = questionRepository.findById(q1.getId()).get();
        assertThat(q1).isSameAs(q2);
    }

    @Test
    @DisplayName("개체를 저장하고 영속성 컨텍스트를 초기화한 후 개체를 다시 가져왔을 때, 저장했을 당시의 개체와 동등한지 테스트")
    void equality() {
        Question q1 = questionRepository.save(new Question(user, "제목1", "내용1"));
        entityManager.clear();
        Question q2 = questionRepository.findById(q1.getId()).get();
        assertThat(q1).isEqualTo(q2);
    }
}
