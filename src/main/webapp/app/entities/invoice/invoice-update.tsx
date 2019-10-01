import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './invoice.reducer';
import { IInvoice } from 'app/shared/model/invoice.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInvoiceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IInvoiceUpdateState {
  isNew: boolean;
}

export class InvoiceUpdate extends React.Component<IInvoiceUpdateProps, IInvoiceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { invoiceEntity } = this.props;
      const entity = {
        ...invoiceEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/invoice');
  };

  render() {
    const { invoiceEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="App.invoice.home.createOrEditLabel">
              <Translate contentKey="App.invoice.home.createOrEditLabel">Create or edit a Invoice</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : invoiceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="invoice-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="invoice-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="invoice_numberLabel" for="invoice-invoice_number">
                    <Translate contentKey="App.invoice.invoice_number">Invoice Number</Translate>
                  </Label>
                  <AvField
                    id="invoice-invoice_number"
                    type="text"
                    name="invoice_number"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      pattern: { value: '^[0-9]+$', errorMessage: translate('entity.validation.pattern', { pattern: '^[0-9]+$' }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="totalLabel" for="invoice-total">
                    <Translate contentKey="App.invoice.total">Total</Translate>
                  </Label>
                  <AvField
                    id="invoice-total"
                    type="text"
                    name="total"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="currencyLabel" for="invoice-currency">
                    <Translate contentKey="App.invoice.currency">Currency</Translate>
                  </Label>
                  <AvInput
                    id="invoice-currency"
                    type="select"
                    className="form-control"
                    name="currency"
                    value={(!isNew && invoiceEntity.currency) || 'USD'}
                  >
                    <option value="USD">{translate('App.Currency.USD')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="invoice_dateLabel" for="invoice-invoice_date">
                    <Translate contentKey="App.invoice.invoice_date">Invoice Date</Translate>
                  </Label>
                  <AvField
                    id="invoice-invoice_date"
                    type="date"
                    className="form-control"
                    name="invoice_date"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="due_dateLabel" for="invoice-due_date">
                    <Translate contentKey="App.invoice.due_date">Due Date</Translate>
                  </Label>
                  <AvField id="invoice-due_date" type="date" className="form-control" name="due_date" />
                </AvGroup>
                <AvGroup>
                  <Label id="vendor_nameLabel" for="invoice-vendor_name">
                    <Translate contentKey="App.invoice.vendor_name">Vendor Name</Translate>
                  </Label>
                  <AvField id="invoice-vendor_name" type="text" name="vendor_name" />
                </AvGroup>
                <AvGroup>
                  <Label id="remittance_addressLabel" for="invoice-remittance_address">
                    <Translate contentKey="App.invoice.remittance_address">Remittance Address</Translate>
                  </Label>
                  <AvField id="invoice-remittance_address" type="text" name="remittance_address" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="invoice-status">
                    <Translate contentKey="App.invoice.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="invoice-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && invoiceEntity.status) || 'PENDING'}
                  >
                    <option value="PENDING">{translate('App.InvoiceStatus.PENDING')}</option>
                    <option value="APPROVED">{translate('App.InvoiceStatus.APPROVED')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/invoice" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  invoiceEntity: storeState.invoice.entity,
  loading: storeState.invoice.loading,
  updating: storeState.invoice.updating,
  updateSuccess: storeState.invoice.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InvoiceUpdate);
