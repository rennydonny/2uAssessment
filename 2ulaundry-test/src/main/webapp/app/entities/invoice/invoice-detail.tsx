import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invoice.reducer';
import { IInvoice } from 'app/shared/model/invoice.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInvoiceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InvoiceDetail extends React.Component<IInvoiceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { invoiceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="twoulaundrytestApp.invoice.detail.title">Invoice</Translate> [<b>{invoiceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="invoiceNumber">
                <Translate contentKey="twoulaundrytestApp.invoice.invoiceNumber">Invoice Number</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.invoiceNumber}</dd>
            <dt>
              <span id="total">
                <Translate contentKey="twoulaundrytestApp.invoice.total">Total</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.total}</dd>
            <dt>
              <span id="currency">
                <Translate contentKey="twoulaundrytestApp.invoice.currency">Currency</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.currency}</dd>
            <dt>
              <span id="invoiceDate">
                <Translate contentKey="twoulaundrytestApp.invoice.invoiceDate">Invoice Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={invoiceEntity.invoiceDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="dueDate">
                <Translate contentKey="twoulaundrytestApp.invoice.dueDate">Due Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={invoiceEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="vendorName">
                <Translate contentKey="twoulaundrytestApp.invoice.vendorName">Vendor Name</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.vendorName}</dd>
            <dt>
              <span id="remittanceAddress">
                <Translate contentKey="twoulaundrytestApp.invoice.remittanceAddress">Remittance Address</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.remittanceAddress}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="twoulaundrytestApp.invoice.status">Status</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.status}</dd>
          </dl>
          <Button tag={Link} to="/entity/invoice" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/invoice/${invoiceEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ invoice }: IRootState) => ({
  invoiceEntity: invoice.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InvoiceDetail);
