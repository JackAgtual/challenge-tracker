import { FieldValues, FormState } from "react-hook-form";

type FormRootErrorProps<TFieldValues extends FieldValues> = {
  formState: FormState<TFieldValues>;
};

export default function FormRootError<TFieldValues extends FieldValues>({
  formState,
}: FormRootErrorProps<TFieldValues>) {
  return (
    <div>
      {formState.errors.root && (
        <>
          <p className="text-red-600">
            Something went wrong: {formState.errors.root?.message}
          </p>
          <p className="text-red-600">Try again</p>
        </>
      )}
    </div>
  );
}
