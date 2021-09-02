import roomData from '../fixtures/example.json';

const GAME_ID = 75;

describe('E2E 테스트', () => {
  before(() => {
    cy.visit('http://localhost:3000');
  });

  it('페이지에 처음 접속하면 슬라이더 이미지를 볼 수 있다.', () => {
    cy.get('.slide').should('be.visible');
  });

  it('페이지에 처음 접속하면 전체 게임 목록을 볼 수 있다.', () => {
    cy.get('.game-card-container', { timeout: 4000 }).should('be.visible');
  });

  it('사용자는 게임 목록에서 원하는 게임을 검색할 수 있다.', () => {
    cy.get('.game-list-container').within(() => {
      cy.get('.input-inner').type('lea');
      cy.get('.keyword-list-container').within(() => {
        cy.get('.keyword-button', { timeout: 4000 })
          .contains('League of Legends')
          .should('be.visible');
      });
    });
  });

  it('사용자는 찾은 게임을 클릭해 방 목록으로 들어갈 수 있다.', () => {
    cy.intercept(
      'GET',
      `https://api.babble.gg/api/rooms?gameId=${GAME_ID}&tagIds=&page=1`,
      {
        fixture: 'example.json',
      }
    ).as('getRooms');

    cy.get('.game-list').within(() => {
      cy.get('.game-card-container').first().click().wait('@getRooms');

      cy.location().should((loc) => {
        expect(loc.pathname).to.eq(`/games/${GAME_ID}`);
      });
    });
  });

  it('방 목록에 접속하면 방 목록을 볼 수 있다.', () => {
    cy.get('.room-list-section')
      .children()
      .each(($room, index) => {
        cy.wrap($room).within(() => {
          cy.get('.about').within(() => {
            cy.get('.room-number')
              .contains(roomData[index].roomId)
              .should('be.visible');

            cy.get('figcaption')
              .contains(roomData[index].host.nickname)
              .should('be.visible');
          });
        });
      });
  });

  it('사용자는 닉네임 변경 아이콘을 눌러 닉네임을 변경할 수 있다.', () => {
    cy.get('.edit').first().click();

    cy.get('.default-container').within(() => {
      cy.get('.input-inner').first().type('코를 파는 라이언');

      cy.get('.control-buttons').within(() => {
        cy.get('.colored').click();
      });
    });

    cy.get('.nickname-section-container').within(() => {
      cy.get('.body2').contains('코를 파는 라이언').should('be.visible');
    });
  });

  it('사용자는 방 생성하기 버튼을 눌러 방 생성 페이지에 들어갈 수 있다.', () => {
    cy.get('.side').within(() => {
      cy.get('.colored').click();

      cy.location().should((loc) => {
        expect(loc.pathname).to.eq(`/games/${GAME_ID}/make-room`);
      });
    });
  });

  it('사용자는 방을 생성하여 채팅 모달창을 띄울 수 있다.', () => {
    cy.get('.make-room-form').within(() => {
      cy.get('input[type="number"]').click();
      cy.get('.keyword-list-container').within(() => {
        cy.get('.keyword-button').first().click();
      });

      cy.get('input[type="search"]').type('1 hour');
      cy.get('.keyword-list-container')
        .eq(1)
        .within(() => {
          cy.get('.keyword-button').first().click();
        });

      cy.get('.colored').click();
    });

    cy.get('.modal-chatting-room').should('be.visible');
  });

  it('방을 생성하면 생성한 방을 볼 수 있다.', () => {
    cy.get('.room-list-section', { timeout: 6000 }).should('be.visible');
  });

  it('사용자는 접속한 채팅방에서 채팅을 할 수 있다.', () => {
    cy.get('.modal-chatting-room').within(() => {
      cy.get('.notice-container', { timeout: 4000 }).should('be.visible');
      cy.get('textarea').type('ㅎㅇㅎㅇ{enter}');
    });

    cy.get('.mine').contains('ㅎㅇㅎㅇ').should('be.visible');
  });

  it('사용자는 접속한 채팅방을 최소화 할 수 있다.', () => {
    cy.get('.modal-chatting-room').within(() => {
      cy.get('.room-minimize').click();
    });

    cy.get('.modal-chatting-room').should('not.be.visible');
  });

  it('사용자는 접속한 채팅방을 최대화 할 수 있다.', () => {
    cy.get('.modal-minimized-container').within(() => {
      cy.get('.maximize').click();
    });

    cy.get('.modal-chatting-room').should('be.visible');
  });
});
