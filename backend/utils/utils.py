def update_object(obj, **kwargs):
    for k, v in kwargs.items():
        setattr(obj, k, v)
    obj.save()