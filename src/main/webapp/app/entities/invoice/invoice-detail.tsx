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
            <Translate contentKey="App.invoice.detail.title">Invoice</Translate> [<b>{invoiceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="invoice_number">
                <Translate contentKey="App.invoice.invoice_number">Invoice Number</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.invoice_number}</dd>
            <dt>
              <span id="total">
                <Translate contentKey="App.invoice.total">Total</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.total}</dd>
            <dt>
              <span id="currency">
                <Translate contentKey="App.invoice.currency">Currency</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.currency}</dd>
            <dt>
              <span id="invoice_date">
                <Translate contentKey="App.invoice.invoice_date">Invoice Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={invoiceEntity.invoice_date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="due_date">
                <Translate contentKey="App.invoice.due_date">Due Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={invoiceEntity.due_date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="vendor_name">
                <Translate contentKey="App.invoice.vendor_name">Vendor Name</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.vendor_name}</dd>
            <dt>
              <span id="remittance_address">
                <Translate contentKey="App.invoice.remittance_address">Remittance Address</Translate>
              </span>
            </dt>
            <dd>{invoiceEntity.remittance_address}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="App.invoice.status">Status</Translate>
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
