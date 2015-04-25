import controllers.Application;
import dao.UserDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.i18n.Lang;
import play.mvc.Http;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static junit.framework.TestCase.assertNull;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by antonkw on 25.04.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecieveUserByTokenTest {
    @Mock
    protected EntityManager em;

    private Http.Context mockFinalContext;

    @Mock
    Http.Request mockReq;

    @Mock
    private Http.Request mockRequest;

    @Mock
    private UserDAO mockDao;

    @Mock
    private Query query;


    private Http.Context getMockContext() {
        when(mockRequest.remoteAddress()).thenReturn("127.0.0.1");
        when(mockRequest.getHeader("User-Agent")).thenReturn("mocked user-agent");
        Http.Context mockContext = mock(Http.Context.class);
        when(mockContext.request()).thenReturn(mockRequest);
        when(mockContext.lang()).thenReturn(Lang.forCode("en"));
        return mockContext;
    }

    @Test
    public void nullCookieCaseTest() {
        mockFinalContext = getMockContext();
        Http.Context.current.set(mockFinalContext);
        assertThat(mockFinalContext.request()).isNotNull();
        verify(mockFinalContext).request();
        when(mockFinalContext.request().cookie(any(String.class))).thenReturn(null);
        when(mockDao.findByToken(any(String.class))).thenReturn(null);
        assertNull(Application.recieveUserByToken());
    }

   /* @Test
    public void notFoundTokenCaseTest() {
      //  EntityManager entityManager = JPA.em("default");
        User user = new User();
        mockFinalContext = getMockContext();
        Http.Context.current.set(mockFinalContext);
        assertThat(mockFinalContext.request()).isNotNull();
        verify(mockFinalContext).request();
        when(mockFinalContext.request().cookie(any(String.class))).thenReturn(new Http.Cookie("token",
                "incorrenct token", 200, "", "", false, false));
        mockFinalContext.args.put("currentEntityManager", em);
       // EntityManager em = mock(EntityManager.class);
        when(JPA.em()).thenReturn(em);
       // when(em.createNamedQuery("UserDAO.findByToken")).thenReturn(query);
      //  when(query.getResultList()).thenReturn(Collections.singletonList(user));
        when(mockDao.findByToken(any(String.class))).thenReturn(null);
        assertNull(Application.recieveUserByToken());
    }*/
}