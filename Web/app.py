import os
from flask import Flask, url_for, redirect, request, flash, render_template, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_admin import Admin, AdminIndexView, expose
from flask_admin.contrib.sqla import ModelView
from flask_wtf.file import FileAllowed
from wtforms import TextAreaField, FileField, SelectField
from markupsafe import Markup
from werkzeug.utils import secure_filename
from flask_login import LoginManager, UserMixin, login_user, login_required, logout_user, current_user
from werkzeug.security import generate_password_hash, check_password_hash
from datetime import datetime
from sqlalchemy import desc
from wtforms import PasswordField
from wtforms.validators import InputRequired
from datetime import datetime, date

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your_secret_key'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///smart_drone.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['UPLOAD_FOLDER'] = '/home/AeroAlert/mysite/static/uploads'

login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

db = SQLAlchemy(app)

# Models
class AdminUser(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(100), unique=True, nullable=False)
    password_hash = db.Column(db.String(200), nullable=False)
    email = db.Column(db.String(100), unique=True, nullable=False)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

class SecurityGuard(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    mobile = db.Column(db.String(20), nullable=False)
    name = db.Column(db.String(100), nullable=False)
    address = db.Column(db.Text, nullable=False)
    email = db.Column(db.String(100), unique=True, nullable=False)
    password_hash = db.Column(db.String(200), nullable=False)
    created_date = db.Column(db.DateTime, default=datetime.utcnow)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

class AlertLog(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    type = db.Column(db.String(50), nullable=True)
    timestamp = db.Column(db.DateTime)
    description = db.Column(db.String(100), nullable=True)
    address = db.Column(db.Text, nullable=True)
    latitude = db.Column(db.String(100), nullable=True)
    longitude = db.Column(db.String(100), nullable=True)
    cam_type = db.Column(db.String(5), nullable=True)





@login_manager.user_loader
def load_user(user_id):
    return AdminUser.query.get(int(user_id))


class BaseView(ModelView):
    can_view_details = True
    can_export = True
    page_size = 25  # Number of items per page
    column_searchable_list = ['name']  # Allow searching by name

# Admin Views
class AuthenticatedBaseView(BaseView):
    def is_accessible(self):
        return current_user.is_authenticated

    def inaccessible_callback(self, name, **kwargs):
        return redirect(url_for('login', next=request.url))

    @property
    def _template_args(self):
        args = super(AuthenticatedBaseView, self)._template_args
        args['admin_base_template'] = 'admin/custom_base.html'
        return args

class AdminUserView(AuthenticatedBaseView):
    column_exclude_list = ['password_hash']
    form_excluded_columns = ['password_hash']
    column_searchable_list = ['username', 'email']
    column_filters = ['email']

    form_extra_fields = {
        'password': PasswordField('Password')
    }

    def on_model_change(self, form, model, is_created):
        if form.password.data:
            model.set_password(form.password.data)

    def create_form(self):
        form = super(AdminUserView, self).create_form()
        form.password.validators = [InputRequired()]
        return form

    def edit_form(self, obj):
        form = super(AdminUserView, self).edit_form(obj)
        form.password.validators = []  # Make password optional for editing
        return form

from wtforms import PasswordField

class SecurityGuardView(AuthenticatedBaseView):
    column_exclude_list = ['password_hash']
    form_excluded_columns = ['password_hash']
    column_searchable_list = ['name', 'email', 'mobile']
    column_filters = ['name', 'email', 'mobile']

    form_extra_fields = {
        'password': PasswordField('Password')
    }

    def on_model_change(self, form, model, is_created):
        if form.password.data:
            model.set_password(form.password.data)

    def create_form(self):
        form = super(SecurityGuardView, self).create_form()
        form.password.validators = [InputRequired()]
        return form

    def edit_form(self, obj):
        form = super(SecurityGuardView, self).edit_form(obj)
        form.password.validators = []  # Make password optional for editing
        return form



class AlertLogView(AuthenticatedBaseView):
    column_searchable_list = ['type', 'description', 'address']
    column_filters = ['type', 'timestamp','cam_type']
    column_exclude_list = ['address','latitude', 'longitude','image_path']  # Exclude parking_time and cam_type from list view
    # can_create = False
    # can_edit   = False



class MyAdminIndexView(AdminIndexView):
    @expose('/')
    @login_required
    def index(self):
        admin_count = AdminUser.query.count()
        guard_count = SecurityGuard.query.count()
        log_count = SecurityGuard.query.count()

        self._template_args['admin_view'] = self
        self._template_args['admin_base_template'] = 'admin/custom_base.html'

        return self.render('admin/index.html', admin_count=admin_count,
                        guard_count=guard_count,log_count=log_count)
    @expose('/logout/')
    @login_required
    def logout_view(self):
        logout_user()
        return redirect(url_for('login'))



admin = Admin(app, name='AeroAlert - Immediate action from the skies.', template_mode='bootstrap4', index_view=MyAdminIndexView())
admin.add_view(AdminUserView(AdminUser, db.session, name='Admin Users'))
admin.add_view(SecurityGuardView(SecurityGuard, db.session, name='Security Guards'))
admin.add_view(AlertLogView(AlertLog, db.session, name='Alert Logs'))

@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('admin.index'))
    if request.method == 'POST':
        user = AdminUser.query.filter_by(username=request.form['username']).first()
        if user and user.check_password(request.form['password']):
            login_user(user)
            return redirect(url_for('admin.index'))
        flash('Invalid username or password')
    return render_template('login.html')

@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('login'))


@app.route('/')
def index():
    return redirect(url_for('login'))


@app.route('/entryGate')
def entryGate():
    return render_template('admin/entry_gate.html')


from werkzeug.security import check_password_hash

@app.route('/gaurdLogin', methods=['POST'])
def gaurdLogin():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    user = SecurityGuard.query.filter_by(email=email).first()

    if user and check_password_hash(user.password_hash, password):
        return jsonify({"message": "Login successful"}), 200
    else:
        return jsonify({"message": "Invalid email or password"}), 401

@app.route('/api/alert_logs', methods=['GET'])
def get_alert_logs():
    try:
        # Get page number and limit from query parameters, with defaults
        page = 1
        limit = 10

        # Query with pagination
        pagination = AlertLog.query.order_by(AlertLog.timestamp.desc()).paginate(page=page, per_page=limit, error_out=False)

        logs_list = []
        for log in pagination.items:
            logs_list.append({
                'id': log.id,
                'type': log.type,
                'timestamp': log.timestamp.strftime('%Y-%m-%d %H:%M:%S'),
                'description': log.description,
                'address': log.address,
                'latitude': log.latitude,
                'longitude': log.longitude,
                'camType': log.cam_type
            })

        return jsonify(logs_list), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/api/log_alert', methods=['POST'])
def log_alert():
    data = request.json
    new_alert = AlertLog(
        type=data.get('type'),
        timestamp=datetime.utcnow(),
        description=data.get('description'),
        address=data.get('address'),
        latitude=data.get('latitude'),
        longitude=data.get('longitude'),
        cam_type=data.get('cam_type')
    )
    db.session.add(new_alert)
    db.session.commit()
    return jsonify({"message": "Alert logged successfully"}), 201

from datetime import datetime, date
from flask import jsonify
from datetime import date
from flask import jsonify
from sqlalchemy import func

@app.route('/api/today_alert_logs', methods=['GET'])
def today_alert_logs():
    try:
        # Get today's date
        today = date.today()

        # Query AlertLog records where the date part of the timestamp matches today's date
        alert_logs = AlertLog.query.filter(
            func.date(AlertLog.timestamp) == today
        ).order_by(AlertLog.timestamp.asc()).all()

        # Create a list of dictionaries to hold the response data
        logs_list = []
        for log in alert_logs:
            logs_list.append({
                'id': log.id,
                'type': log.type,
                'timestamp': log.timestamp.strftime('%Y-%m-%d %H:%M:%S'),
                'description': log.description,
                'address': log.address,
                'latitude': log.latitude,
                'longitude': log.longitude,
                'camType': log.cam_type
            })

        return jsonify(logs_list), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 400


if __name__ == '__main__':
    with app.app_context():
        db.create_all()
        if not AdminUser.query.filter_by(username='admin').first():
            admin_user = AdminUser(username='admin', email='admin@gmail.com')
            admin_user.set_password('admin123')
            db.session.add(admin_user)
            db.session.commit()

    # app.run(debug=True)