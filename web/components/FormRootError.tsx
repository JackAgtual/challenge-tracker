import { FieldValues, FormState } from "react-hook-form";

type Props<TFieldValues extends FieldValues> = {
  formState: FormState<TFieldValues>;
};

export function FormRootError<TFieldValues extends FieldValues>({
  formState,
}: Props<TFieldValues>) {
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
