package com.squff.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.squff.IntegrationTest;
import com.squff.domain.Order;
import com.squff.domain.enumeration.Status;
import com.squff.repository.OrderRepository;
import com.squff.service.EntityManager;
import com.squff.service.dto.OrderDTO;
import com.squff.service.mapper.OrderMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class OrderResourceIT {

    private static final Long DEFAULT_GENERATED_CODE = 1L;
    private static final Long UPDATED_GENERATED_CODE = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SHIPPED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SHIPPED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECIEVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECIEVED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.OPENED;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .generatedCode(DEFAULT_GENERATED_CODE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .shippedAt(DEFAULT_SHIPPED_AT)
            .recievedAt(DEFAULT_RECIEVED_AT)
            .status(DEFAULT_STATUS)
            .isActive(DEFAULT_IS_ACTIVE);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .generatedCode(UPDATED_GENERATED_CODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .shippedAt(UPDATED_SHIPPED_AT)
            .recievedAt(UPDATED_RECIEVED_AT)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE);
        return order;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Order.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        order = createEntity(em);
    }

    @Test
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().collectList().block().size();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getGeneratedCode()).isEqualTo(DEFAULT_GENERATED_CODE);
        assertThat(testOrder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrder.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOrder.getShippedAt()).isEqualTo(DEFAULT_SHIPPED_AT);
        assertThat(testOrder.getRecievedAt()).isEqualTo(DEFAULT_RECIEVED_AT);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrder.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        int databaseSizeBeforeCreate = orderRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrdersAsStream() {
        // Initialize the database
        orderRepository.save(order).block();

        List<Order> orderList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(OrderDTO.class)
            .getResponseBody()
            .map(orderMapper::toEntity)
            .filter(order::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(orderList).isNotNull();
        assertThat(orderList).hasSize(1);
        Order testOrder = orderList.get(0);
        assertThat(testOrder.getGeneratedCode()).isEqualTo(DEFAULT_GENERATED_CODE);
        assertThat(testOrder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrder.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOrder.getShippedAt()).isEqualTo(DEFAULT_SHIPPED_AT);
        assertThat(testOrder.getRecievedAt()).isEqualTo(DEFAULT_RECIEVED_AT);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrder.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    void getAllOrders() {
        // Initialize the database
        orderRepository.save(order).block();

        // Get all the orderList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(order.getId().intValue()))
            .jsonPath("$.[*].generatedCode")
            .value(hasItem(DEFAULT_GENERATED_CODE.intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].shippedAt")
            .value(hasItem(DEFAULT_SHIPPED_AT.toString()))
            .jsonPath("$.[*].recievedAt")
            .value(hasItem(DEFAULT_RECIEVED_AT.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    void getOrder() {
        // Initialize the database
        orderRepository.save(order).block();

        // Get the order
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, order.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(order.getId().intValue()))
            .jsonPath("$.generatedCode")
            .value(is(DEFAULT_GENERATED_CODE.intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.shippedAt")
            .value(is(DEFAULT_SHIPPED_AT.toString()))
            .jsonPath("$.recievedAt")
            .value(is(DEFAULT_RECIEVED_AT.toString()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingOrder() {
        // Get the order
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).block();
        updatedOrder
            .generatedCode(UPDATED_GENERATED_CODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .shippedAt(UPDATED_SHIPPED_AT)
            .recievedAt(UPDATED_RECIEVED_AT)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, orderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getGeneratedCode()).isEqualTo(UPDATED_GENERATED_CODE);
        assertThat(testOrder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrder.getShippedAt()).isEqualTo(UPDATED_SHIPPED_AT);
        assertThat(testOrder.getRecievedAt()).isEqualTo(UPDATED_RECIEVED_AT);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, orderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .shippedAt(UPDATED_SHIPPED_AT)
            .recievedAt(UPDATED_RECIEVED_AT)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getGeneratedCode()).isEqualTo(DEFAULT_GENERATED_CODE);
        assertThat(testOrder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrder.getShippedAt()).isEqualTo(UPDATED_SHIPPED_AT);
        assertThat(testOrder.getRecievedAt()).isEqualTo(UPDATED_RECIEVED_AT);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .generatedCode(UPDATED_GENERATED_CODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .shippedAt(UPDATED_SHIPPED_AT)
            .recievedAt(UPDATED_RECIEVED_AT)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getGeneratedCode()).isEqualTo(UPDATED_GENERATED_CODE);
        assertThat(testOrder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrder.getShippedAt()).isEqualTo(UPDATED_SHIPPED_AT);
        assertThat(testOrder.getRecievedAt()).isEqualTo(UPDATED_RECIEVED_AT);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, orderDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrder() {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeDelete = orderRepository.findAll().collectList().block().size();

        // Delete the order
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, order.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
